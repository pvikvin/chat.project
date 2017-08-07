package com.gemicle.websocket;

import java.io.IOException;
import java.util.logging.Logger;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.gemicle.chatweb.constants.Constants;
import com.gemicle.chatweb.message.generator.MessageConnectGenerator;
import com.gemicle.chatweb.message.generator.MessageDisconnectGenerator;
import com.gemicle.chatweb.message.generator.MessageTextGenerator;
import com.gemicle.chatweb.senders.MessageSender;
import com.gemicle.interfaces.MessageGenerator;
import com.gemicle.managers.UserDb;
import com.gemicle.managers.UserManager;
import com.gemicle.pojo.Message;
import com.gemicle.pojo.User;
import com.gemicle.utils.MessageDecoder;
import com.gemicle.utils.MessageEncoder;

@ServerEndpoint(value = "/chat/{username}", decoders = MessageDecoder.class, encoders = MessageEncoder.class)
public class ChatEndpoint {

	private Session session;
	private UserDb userDb = new UserDb();
	private static Logger log = Logger.getLogger(ChatEndpoint.class.getName());
	private User user;
	private MessageGenerator messageGenerator;
	private MessageSender sender = new MessageSender();

	public ChatEndpoint() {
		log.info("Create ChatEndpoint");
	}

	@OnOpen
	public void onOpen(Session session, @PathParam("username") String username) throws IOException, EncodeException {
		log.info("Open websoket");
		this.session = session;
		Constants.CHAT_END_POINTS.add(this);
		user = userDb.save(session.getId(), username);
		log.info("users: " + UserManager.getUsers());
		messageGenerator = new MessageConnectGenerator(user);
		sender.sendMessage(messageGenerator);
	}

	@OnMessage
	public void onMessage(Session session, Message message) throws IOException, EncodeException {
		log.info("onMessage: " + message.toString());
		messageGenerator = new MessageTextGenerator(user, message);
		sender.sendMessage(messageGenerator);
	}

	@OnClose
	public void onClose(Session session) throws IOException, EncodeException {
		Constants.CHAT_END_POINTS.remove(this);
		user = userDb.getUserFromSessionId(session.getId());
		messageGenerator = new MessageDisconnectGenerator(user);
		sender.sendMessage(messageGenerator);
		userDb.delete(session.getId());	
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		// Do error handling here
	}

	public Session getSession() {
		return session;
	}

}
