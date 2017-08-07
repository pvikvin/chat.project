package com.gemicle.chatweb.message.generator;

import com.gemicle.interfaces.MessageGenerator;
import com.gemicle.pojo.Message;
import com.gemicle.pojo.User;

public class MessageConnectGenerator implements MessageGenerator{

	private User user;
	
	public MessageConnectGenerator(User user){
		this.user = user;
	}

	public Message generateMessage() {
		Message message = new Message();
		message.setFromUser(user.getLogin());
		message.setContent("Connected!");
		message.setUserIdFrom(user.getId());
		message.setToUser("All users");
		return message;
	}
	
	
}
