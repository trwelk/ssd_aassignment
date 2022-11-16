	package com.demo.user.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Table(name="user")
@Entity
public class User implements Serializable {

	private static final long serialVersionUID = 983648238746032841L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="user_id")
	private int userId;
	 
	
	@Column(name="user_name")
	private String userName;
	
	@Column(name="password")
	private String password;

	@Column(name="role_role_id")
	private String roleId;
	
	@Column(name="user_type")
	private String userType;
	
	@Column(name="email")
	private String emailId;
	
	@ManyToOne()
    @JoinColumn(name = "role_role_id",referencedColumnName = "role_id",insertable = false,updatable = false)
	private Role role;
	
	@ManyToMany(mappedBy = "utilityConsumers", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	private Set<Utility> consumedUtilitites;
	
	@ManyToMany(mappedBy = "utilityProviders", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	private Set<Utility> providedUtilitites;
	

	public User() {
		super();
	}

		
	public User(int userId, String userName, String userType, String password) {
		this.userId = userId;
		this.userName = userName;
		this.password=password;
		this.userType = userType;
		
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


	public Set<Utility> getConsumedUtilitites() {
		return consumedUtilitites;
	}


	public void setConsumedUtilitites(Set<Utility> consumedUtilitites) {
		this.consumedUtilitites = consumedUtilitites;
	}


	public Set<Utility> getProvidedUtilitites() {
		return providedUtilitites;
	}


	public void setProvidedUtilitites(Set<Utility> providedUtilitites) {
		this.providedUtilitites = providedUtilitites;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public Role getRole() {
		return role;
	}


	public String getRoleId() {
		return roleId;
	}


	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}


	
	
}
