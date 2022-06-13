package com.scube.chargingstation.service;

import java.util.List;

import javax.validation.Valid;

import com.scube.chargingstation.dto.AuthUserDto;
import com.scube.chargingstation.dto.incoming.OtpVerificationIncomingDto;
import com.scube.chargingstation.dto.incoming.UserInfoIncomingDto;

public interface UserInfoService {

	boolean addUserInfo(@Valid UserInfoIncomingDto userInfoIncomingDto);

	boolean moblieOtpVerify(@Valid OtpVerificationIncomingDto otpVerificationIncomingDto);
	
	boolean editUserProfile(UserInfoIncomingDto userInfoIncomingDto);
	
	boolean deleteUserProfile(String userId);

	List<AuthUserDto> getAllUser();

	AuthUserDto getUserById(String userId);
	
	AuthUserDto getPartnerUserById(String userId);
	

	List<AuthUserDto> getAllPartnerUsers(String string);
	int findCountForWeekNewAddedUser();

	List<AuthUserDto> getPartnerUsersByRoleCode(String string);
}
