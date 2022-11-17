package com.sliit.ssd.client.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;



public class User implements Serializable {

//	private static final long serialVersionUID = 983648238746032841L;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private int userId;
	
	private String userName;
	
	
	private String password;
	
	private String userType;
	
	
	private String emailId;
	
	private int roleId;
	
	private Role role;
	

	public User() {
		super();
	}

		
	public User(int userId, String userName, String userType, String password) {
		this.userId = userId;
		this.userName = userName;
		this.password=password;
		
	}
	
	public User(String userName, String userType, String password) {
		
		this.userName = userName;
		this.password=password;
	}

	public User (User user) {
		super();
		this.userId = user.getUserId();
		this.userName = user.getUserName();
		this.password=user.getPassword();
		this.userType = user.getUserType();
		this.emailId = user.getEmailId();
		this.role=user.getRoles();
	}
	

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	

	public int getRoleId() {
		return roleId;
	}


	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}


	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public Role getRoles() {
		return role;
	}

	public void setRole(Role roles) {
		this.role = roles;
	}
	
}