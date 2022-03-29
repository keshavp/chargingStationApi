package com.scube.chargingstation.service;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.scube.chargingstation.entity.UserInfoEntity;
import com.scube.chargingstation.repository.UserInfoRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	
	@Autowired
//	UserRepository userRepository;
	UserInfoRepository	empInfoRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		UserInfoEntity user = empInfoRepository.findByUsername(username);

		return UserDetailsImpl.build(user);
	}

}