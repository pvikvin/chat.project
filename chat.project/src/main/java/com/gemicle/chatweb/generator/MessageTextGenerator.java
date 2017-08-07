package com.gemicle.chatweb.generator;

import javax.websocket.Session;

import com.gemicle.interfaces.MessageGenerator;
import com.gemicle.pojo.Message;

public class MessageTextGenerator implements MessageGenerator{

	private Session session;
	
	public MessageTextGenerator(Session session){
		this.session = session;
	}
	
	@Override
	public Message generateMessage() {
		
		return null;
	}
	
}
