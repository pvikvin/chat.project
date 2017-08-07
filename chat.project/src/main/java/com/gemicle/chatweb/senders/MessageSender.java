package com.gemicle.chatweb.senders;

import java.io.IOException;

import javax.websocket.EncodeException;

import com.gemicle.chatweb.constants.Constants;
import com.gemicle.interfaces.HibernateService;
import com.gemicle.interfaces.MessageGenerator;
import com.gemicle.pojo.Message;
import com.gemicle.service.MessageService;
import com.gemicle.websocket.ChatEndpoint;

public class MessageSender {
	private static HibernateService<Message> messageService = new MessageService();

	public void sendMessage(MessageGenerator messageGenerator) {
		Message message = messageGenerator.generateMessage();
		messageService.save(message);
		for (ChatEndpoint endpoint : Constants.CHAT_END_POINTS) {
			synchronized (endpoint) {
				try {
					endpoint.getSession().getBasicRemote().sendObject(message);
				} catch (IOException | EncodeException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
