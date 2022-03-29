package com.scube.chargingstation.service;

import static com.scube.chargingstation.exception.ExceptionType.DUPLICATE_ENTITY;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.scube.chargingstation.dto.incoming.UserInfoIncomingDto;
import com.scube.chargingstation.entity.UserInfoEntity;
import com.scube.chargingstation.exception.BRSException;
import com.scube.chargingstation.exception.EntityType;
import com.scube.chargingstation.repository.UserInfoRepository;

@Service
public class UserInfoServiceImpl implements UserInfoService {

	private static final Logger logger = LoggerFactory.getLogger(UserInfoServiceImpl.class);
	
	@Autowired
	RoleService	 roleService;
	
	@Autowired
	UserInfoRepository userInfoRepository;
	
	BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	
	@Override
	public boolean addUserInfo(@Valid UserInfoIncomingDto userInfoIncomingDto) {
		
		logger.info("********UserInfoServiceImpl addUserInfo********");
		
		
		UserInfoEntity userCodeDuplicateCheck = userInfoRepository.findByUsername(userInfoIncomingDto.getUsername());
		if(userCodeDuplicateCheck != null) {
			
			logger.error("throw error that user already exists for Username = "+ userInfoIncomingDto.getUsername());
			throw BRSException.throwException(EntityType.USER, DUPLICATE_ENTITY, userInfoIncomingDto.getUsername());
		}
		
		UserInfoEntity userEmailDuplicateCheck = userInfoRepository.findByMobilenumber(userInfoIncomingDto.getMobilenumber());
		if(userEmailDuplicateCheck != null) {
			logger.error("throw error that Mobilenumber already exists for Mobilenumber = "+ userInfoIncomingDto.getMobilenumber());
			throw BRSException.throwException(EntityType.USER, DUPLICATE_ENTITY, userInfoIncomingDto.getMobilenumber());
		}
		
		UserInfoEntity userInfoEntity = new UserInfoEntity();
		
		
		userInfoEntity.setUsername(userInfoIncomingDto.getUsername()); 

		userInfoEntity.setEmail(userInfoIncomingDto.getEmail());
		userInfoEntity.setMobilenumber(userInfoIncomingDto.getMobilenumber());
		System.out.println("-------------userInfoIncomingDto.getPassword()---------------"+userInfoIncomingDto.getPassword());
		
		userInfoEntity.setPassword(encoder.encode(userInfoIncomingDto.getPassword()));
		System.out.println("-------------userInfoIncomingDto.getPassword()---------------"+userInfoEntity.getPassword() );
		userInfoEntity.setStatus(userInfoIncomingDto.getStatus());
		
		userInfoEntity.setRole(roleService.findRoleNameByCode(userInfoIncomingDto.getRole()));

		userInfoEntity.setResetpasswordcount(0);
		userInfoEntity.setVersion(1);
		userInfoEntity.setResetpassword("N");

		userInfoRepository.save(userInfoEntity);

		return true;
	}

}
