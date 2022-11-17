package com.demo.user.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.user.iservice.IMessageService;
import com.demo.user.model.Message;
import com.demo.user.model.User;
import com.demo.user.repository.MessageRepository;
import com.demo.user.repository.UserRepository;

@Service
public class MessageService implements IMessageService{

	@Autowired
	MessageRepository messageRepo;
	
	Logger log=LoggerFactory.getLogger(MessageService.class);

	
	@Override
	public void save(Message message) {
		log.info("saving user");
		messageRepo.save(message);		
	}

	@Override
	public Optional<Message> update(int messageId, Message message) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(int messageId) {
		messageRepo.deleteById(messageId);
		
	}

	@Override
	public List<Message> getMessages() {
		List<Message> messages=messageRepo.findAll();
		return messages;
	}

	@Override
	public List<Message> getMessagesByUserId(int userId) {
		List<Message> messages=messageRepo.findByUserId(userId);
		return messages;
	}

}
