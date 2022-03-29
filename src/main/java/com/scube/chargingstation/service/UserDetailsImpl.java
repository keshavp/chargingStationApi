package com.scube.chargingstation.service;
import java.util.Collection;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.scube.chargingstation.entity.UserInfoEntity;

public class UserDetailsImpl implements UserDetails {
	private static final long serialVersionUID = 1L;

	private String id;
	private String username;
	private String email;
	private String mobilenumber;
	private String role;
	
	@JsonIgnore
	private String password;
	private Long authorities;

	public UserDetailsImpl(String id,  String username, String email, String password,String mobilenumber) {
		
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.mobilenumber = mobilenumber;
	}

	public static UserDetailsImpl build(UserInfoEntity user) {
		return new UserDetailsImpl(
				user.getId(),
				user.getUsername(),
				user.getEmail(),
				user.getPassword(), 
				user.getMobilenumber());
	}

	public String getId() {
		return id;
	}
	@Override
	public String getUsername() {
		return username;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getMobilenumber() {
		return mobilenumber;
	}
	
	public Long getRole() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
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

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserDetailsImpl user = (UserDetailsImpl) o;
		return Objects.equals(id, user.id);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}


}
