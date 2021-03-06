package com.gemicle.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Message {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id; 
	
	private long userIdFrom;
	
	private String fromUser;
    private String toUser;
    private String content;
    
    public Message(){}
}
