package com.sliit.ssd.client.model;

public class Message {

	private int userId;
	
	private int messageId;
	
	
	private String message;


	public int getUserId() {
		return userId;
	}


	public void setUserId(int userId) {
		this.userId = userId;
	}


	public int getMessageId() {
		return messageId;
	}


	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public Message() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Message(int userId, int messageId, String message) {
		super();
		this.userId = userId;
		this.messageId = messageId;
		this.message = message;
	}
	
}
