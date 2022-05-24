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

	List<AuthUserDto> getAllUser();

	AuthUserDto getUserById(String userid);

}
