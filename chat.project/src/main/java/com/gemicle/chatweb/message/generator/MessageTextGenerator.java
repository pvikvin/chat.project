package com.gemicle.chatweb.message.generator;

import com.gemicle.interfaces.MessageGenerator;
import com.gemicle.pojo.Message;
import com.gemicle.pojo.User;

public class MessageTextGenerator implements MessageGenerator{

	private User user;
	private Message message;
	
	public MessageTextGenerator(User user, Message message){
		this.user = user;
		this.message = message;
	}

	public Message generateMessage() {
		message.setFromUser(user.getLogin());
		message.setUserIdFrom(user.getId());
		message.setToUser("All users");
		return message;
	}
	
	
}
