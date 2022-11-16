package com.sliit.ssd.auth.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


public class UserDetailsImpl implements UserDetails {

	private static final long serialVersionUID = 1L;

	private User user;
	
	public UserDetailsImpl(User user) {
		super();
		this.user=user;
		
//		System.out.println("user in userdetails impl "+user.getUserName()+"  "+user.getPassword());
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		List<GrantedAuthority> ga=new ArrayList<GrantedAuthority>();
					
		
		ga.add(new SimpleGrantedAuthority("ROLE_"+this.user.getRole().getRoleName().toUpperCase()));
		this.user.getRole().getPermissions().forEach(permission->{
				ga.add(new SimpleGrantedAuthority(permission.getPermissionName().toUpperCase()));
			});
			
		return ga;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.user.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.user.getUserName();
	}

	@Override
	public boolean isAccountNonExpired() {
		
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		
		return true;
	}

	@Override
	public boolean isEnabled() {
		
		return true;
	}
}
