package com.example.demo.entities;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserDetail implements UserDetails {

	
	private Users user;
	

    public UserDetail() {
    }
	
	public UserDetail(Users user) {
		this.user = user;
	}
	

	public UserDetail(Optional<Users> user2) {
		this.user = user;
	}


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole()));

	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		System.out.println(user.getPassword());
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getUsername();
	}

	
}
