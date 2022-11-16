package com.sliit.ssd.auth.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;


import com.sliit.ssd.auth.model.ApplicationClient;
import com.sliit.ssd.auth.model.ClientDetailsImpl;
import com.sliit.ssd.auth.repository.ClientRepository;

@Service
public class ClientDetailsServiceImpl implements ClientDetailsService{
	@Autowired
	private ClientRepository cRepo;


	@Override
	public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {

		ApplicationClient c=cRepo.findByClientId(clientId);
		
		if(c==null)
			throw new ClientRegistrationException("client with "+clientId +" is not available");
		

		
		return new ClientDetailsImpl(c);
	}
}
