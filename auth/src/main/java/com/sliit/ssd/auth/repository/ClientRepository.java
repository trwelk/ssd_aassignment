package com.sliit.ssd.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sliit.ssd.auth.model.ApplicationClient;

public interface ClientRepository extends JpaRepository<ApplicationClient, String> {
	ApplicationClient findByClientId(String clientId);

}
