package com.demo.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.user.model.Message;
import com.demo.user.model.User;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer>{
	List<Message> findByUserId(int userId);

}
