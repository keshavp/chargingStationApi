package com.scube.chargingstation.service;

import static com.scube.chargingstation.exception.ExceptionType.ALREADY_EXIST;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.scube.chargingstation.dto.ConnectorDto;
import com.scube.chargingstation.dto.UserInfoOtpDto;
import com.scube.chargingstation.dto.incoming.OtpVerificationIncomingDto;
import com.scube.chargingstation.dto.incoming.UserInfoIncomingDto;
import com.scube.chargingstation.entity.UserInfoEntity;
import com.scube.chargingstation.entity.UserInfoOtpEntity;
import com.scube.chargingstation.exception.BRSException;
import com.scube.chargingstation.exception.EntityType;
import com.scube.chargingstation.repository.UserInfoRepository;
import com.scube.chargingstation.service.api.SmsService;
import com.scube.chargingstation.util.RandomNumber;

@Service
public class UserInfoServiceImpl implements UserInfoService {

	private static final Logger logger = LoggerFactory.getLogger(UserInfoServiceImpl.class);
	
	@Autowired
	RoleService	 roleService;
	
	@Autowired
	UserInfoRepository userInfoRepository;
	
	@Autowired
	UserInfoOtpService userInfoOtpService;
	
	@Autowired
	SmsService	smsService;
	
	
	
	BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	
	@Override
	public boolean addUserInfo(@Valid UserInfoIncomingDto userInfoIncomingDto) {
		
		logger.info("********UserInfoServiceImpl addUserInfo********");
		
		UserInfoOtpDto userInfoOtpDto = new UserInfoOtpDto();
		
		if(userInfoIncomingDto.getMobilenumber() == "") {
			
			throw BRSException.throwException("user Mobile Number can't be blank");
		}
		
		if(userInfoIncomingDto.getRole() == "") {
			
			throw BRSException.throwException("user role can't be blank");
		}
		
		if(userInfoIncomingDto.getPassword() == "") {
			
			throw BRSException.throwException("user password can't be blank");
		}
		
		
		/*
		 * UserInfoEntity userCodeDuplicateCheck =
		 * userInfoRepository.findByUsername(userInfoIncomingDto.getUsername());
		 * if(userCodeDuplicateCheck != null) {
		 * 
		 * logger.error("throw error that user already exists for Username = "+
		 * userInfoIncomingDto.getUsername()); throw
		 * BRSException.throwException(EntityType.USER, ALREADY_EXIST,
		 * userInfoIncomingDto.getUsername()); }
		 */
		
		UserInfoEntity userEmailDuplicateCheck = userInfoRepository.findByMobilenumber(userInfoIncomingDto.getMobilenumber());
		if(userEmailDuplicateCheck != null) {
			logger.error("throw error that Mobilenumber already exists for Mobilenumber = "+ userInfoIncomingDto.getMobilenumber());
			throw BRSException.throwException(EntityType.USER, ALREADY_EXIST, userInfoIncomingDto.getMobilenumber());
		}
		
		UserInfoEntity userInfoEntity = new UserInfoEntity();
		
		
		userInfoEntity.setUsername(userInfoIncomingDto.getUsername()); 

		userInfoEntity.setEmail(userInfoIncomingDto.getEmail());
		userInfoEntity.setMobilenumber(userInfoIncomingDto.getMobilenumber());
		System.out.println("-------------userInfoIncomingDto.getPassword()---------------"+userInfoIncomingDto.getPassword());
		
		userInfoEntity.setPassword(encoder.encode(userInfoIncomingDto.getPassword()));
		System.out.println("-------------userInfoIncomingDto.getPassword()---------------"+userInfoEntity.getPassword() );
		userInfoEntity.setStatus("ACTIVE");
		
		userInfoEntity.setRole(roleService.findRoleNameByCode(userInfoIncomingDto.getRole()));

		userInfoEntity.setResetpasswordcount(0);
		userInfoEntity.setVersion(1);
		userInfoEntity.setResetpassword("N");
		userInfoEntity.setVerified("N");
		
		userInfoEntity = userInfoRepository.save(userInfoEntity);

		String otpCode = "";
		
		otpCode	= RandomNumber.getRandomNumberString();
		
		UserInfoOtpEntity	userInfoOtpEntity = new UserInfoOtpEntity();
		
		userInfoOtpEntity.setMobilenumber(userInfoIncomingDto.getMobilenumber());
		userInfoOtpEntity.setOtpCode(otpCode);
		userInfoOtpEntity.setUserInfoEntity(userInfoEntity);
		userInfoOtpEntity.setStatus("open");
		
		userInfoOtpService.insertOtpDate(userInfoOtpEntity);
		
		smsService.sendSignupOTPMobile(otpCode,userInfoIncomingDto.getMobilenumber());
		
		
		return true;
	}

	@Override
	public boolean moblieOtpVerify(@Valid OtpVerificationIncomingDto otpVerificationIncomingDto) {
		// TODO Auto-generated method stub
		
		return userInfoOtpService.moblieOtpVerify(otpVerificationIncomingDto);
	}

}
