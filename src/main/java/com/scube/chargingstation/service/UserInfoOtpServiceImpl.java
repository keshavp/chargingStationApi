package com.scube.chargingstation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scube.chargingstation.dto.incoming.OtpVerificationIncomingDto;
import com.scube.chargingstation.entity.UserInfoEntity;
import com.scube.chargingstation.entity.UserInfoOtpEntity;
import com.scube.chargingstation.exception.BRSException;
import com.scube.chargingstation.exception.EntityType;
import com.scube.chargingstation.exception.ExceptionType;
import com.scube.chargingstation.repository.UserInfoOtpRepository;
import com.scube.chargingstation.repository.UserInfoRepository;

@Service
public class UserInfoOtpServiceImpl implements UserInfoOtpService {

	@Autowired
	UserInfoOtpRepository userInfoOtpRepository ;
	
	@Autowired
	UserInfoRepository userInfoRepository ;
	
	@Override
	public void insertOtpDate(UserInfoOtpEntity userInfoOtpEntity) {
		// TODO Auto-generated method stub
		
		userInfoOtpRepository.save(userInfoOtpEntity);
	}

	@Override
	public boolean moblieOtpVerify(OtpVerificationIncomingDto otpVerificationIncomingDto) {
		// TODO Auto-generated method stub
		
		UserInfoOtpEntity userInfoOtpEntity =  userInfoOtpRepository.findByMobilenumberAndOtpCodeByOrderByCreatedAt(otpVerificationIncomingDto.getMobilenumber(),otpVerificationIncomingDto.getOtp());
		
		if(userInfoOtpEntity == null) {
			
			 throw BRSException.throwException(EntityType.OTP, ExceptionType.ENTITY_NOT_FOUND ,""); 
		}
		
		UserInfoEntity userInfoEntity = userInfoOtpEntity.getUserInfoEntity();
		
		userInfoEntity.setVerified("Y");
		
		userInfoRepository.save(userInfoEntity);
		
		userInfoOtpEntity.setStatus("close");
		
		userInfoOtpRepository.save(userInfoOtpEntity);
		
		return true;
	}

	@Override
	public void removeUnVerificationUser() {
		// TODO Auto-generated method stub
		
		List<UserInfoOtpEntity> userInfoOtpEntities  =  userInfoOtpRepository.findOpenStatusMoreThenOneMinutes(); 
		
		for( UserInfoOtpEntity userInfoOtpEntity : userInfoOtpEntities) {

			userInfoOtpRepository.delete(userInfoOtpEntity);
			userInfoRepository.delete(userInfoOtpEntity.getUserInfoEntity());
			
		//	UserInfoEntity userInfoEntity = userInfoService.getUserInfoEntityByIdAndDelete(userInfoOtpEntity.getUserInfoEntity().getId());
			
		}
	}
}
