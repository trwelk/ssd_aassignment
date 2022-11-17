package com.demo.user.iservice;

import java.util.List;
import java.util.Optional;

import com.demo.user.model.Message;
import com.demo.user.model.User;

public interface IMessageService {
	public void save(Message message);
	
	public Optional<Message> update(int messageId,Message message);
	
	public void delete(int messageId);
	
	public List<Message> getMessages();
	public List<Message> getMessagesByUserId(int userId);
	
	
}
