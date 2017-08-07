package com.gemicle.websocket;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.gemicle.interfaces.ServiceHibernate;
import com.gemicle.managers.UserDb;
import com.gemicle.managers.UserManager;
import com.gemicle.pojo.Message;
import com.gemicle.service.MessageService;
import com.gemicle.utils.MessageDecoder;
import com.gemicle.utils.MessageEncoder;

@ServerEndpoint(value = "/chat/{username}", decoders = MessageDecoder.class, encoders = MessageEncoder.class)
public class ChatEndpoint {

	private Session session;
	private static final Set<ChatEndpoint> CHAT_END_POINTS = new HashSet<ChatEndpoint>();
	private static ServiceHibernate<Message> messageService = new MessageService();
	private UserDb userDb = new UserDb();
	private static Logger log = Logger.getLogger(ChatEndpoint.class.getName());

	public ChatEndpoint() {
		log.info("Create ChatEndpoint");
	}

	@OnOpen
	public void onOpen(Session session, @PathParam("username") String username) throws IOException, EncodeException {
		log.info("Open websoket");

		this.session = session;
		CHAT_END_POINTS.add(this);

		userDb.save(session.getId(), username);

		log.info("users: " + UserManager.getUsers());

		Message message = new Message();
		message.setFrom(username);
		message.setContent("Connected!");
		message.setUserIdFrom(userDb.getUserFromSessionId(session.getId()).getId());
		broadcast(message);
	}

	@OnMessage
	public void onMessage(Session session, Message message) throws IOException, EncodeException {
		log.info("onMessage");
		
		message.setFrom(userDb.getUserFromSessionId(session.getId()).getLogin());
		message.setUserIdFrom(userDb.getUserFromSessionId(session.getId()).getId());
		broadcast(message);
	}

	@OnClose
	public void onClose(Session session) throws IOException, EncodeException {
		CHAT_END_POINTS.remove(this);

		userDb.delete(session.getId());

		Message message = new Message();
		message.setFrom(userDb.getUserFromSessionId(session.getId()).getLogin());
		message.setContent("Disconnected!");
		message.setUserIdFrom(userDb.getUserFromSessionId(session.getId()).getId());		
		broadcast(message);
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		// Do error handling here
	}

	private static void broadcast(Message message) throws IOException, EncodeException {
		
		messageService.save(message);
		
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
