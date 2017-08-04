package com.gemicle.websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Logger;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.gemicle.pojo.Message;

@ServerEndpoint(value = "/chat/{username}", decoders = MessageDecoder.class, encoders = MessageEncoder.class)
public class ChatEndpoint {

	private Session session;
	private static final Set<ChatEndpoint> CHAT_END_POINTS = new CopyOnWriteArraySet<ChatEndpoint>();
	private static HashMap<String, String> users = new HashMap<String, String>();
	private static Logger log = Logger.getLogger(ChatEndpoint.class.getName());
	
	public ChatEndpoint(){
		log.info("Create ChatEndpoint");
	}

	@OnOpen
	public void onOpen(Session session, @PathParam("username") String username) throws IOException, EncodeException {
		log.info("Open websoket");
		
		this.session = session;
		CHAT_END_POINTS.add(this);
		users.put(session.getId(), username);
		log.info("users: " + users);
		
		Message message = new Message();
		message.setFrom(username);
		message.setContent("Connected!");
		broadcast(message);
	}

	@OnMessage
	public void onMessage(Session session, Message message) throws IOException, EncodeException {
		log.info("onMessage");
		
		message.setFrom(users.get(session.getId()));
		broadcast(message);
	}

	@OnClose
	public void onClose(Session session) throws IOException, EncodeException {
		CHAT_END_POINTS.remove(this);
		Message message = new Message();
		message.setFrom(users.get(session.getId()));
		message.setContent("Disconnected!");
		broadcast(message);
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		// Do error handling here
	}

	private static void broadcast(Message message) throws IOException, EncodeException {
		CHAT_END_POINTS.forEach(endpoint -> {
			synchronized (endpoint) { 
				try {
					endpoint.session.getBasicRemote().sendObject(message);
				} catch (IOException | EncodeException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
