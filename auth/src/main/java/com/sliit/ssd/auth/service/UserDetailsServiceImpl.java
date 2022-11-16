package com.sliit.ssd.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sliit.ssd.auth.model.User;
import com.sliit.ssd.auth.model.UserDetailsImpl;
import com.sliit.ssd.auth.repository.UserRepository;

@Service
public class UserDetailsServiceImpl  implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user=userRepository.findByUserName(username);
		System.out.println("inside user details Service");
		if(user==null) {
			throw new UsernameNotFoundException(username+" not found");
		}
		
		return new UserDetailsImpl(user);
	}

}
