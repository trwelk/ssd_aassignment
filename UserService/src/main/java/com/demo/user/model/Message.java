package com.demo.user.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.demo.user.config.AttributeEncryptor;

@Table(name="message")
@Entity
public class Message implements Serializable{

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="message_id")
	private int messageId;
	
	@Column(name="user_id")
	private int userId;
	
    @Convert(converter = AttributeEncryptor.class)
	@Column(name="message")
	private String message;

	public Message() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Message(int messageId, int userId, String message) {
		super();
		this.messageId = messageId;
		this.userId = userId;
		this.message = message;
	}

	public Message(int userId, String message) {
		super();
		this.userId = userId;
		this.message = message;
	}

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
