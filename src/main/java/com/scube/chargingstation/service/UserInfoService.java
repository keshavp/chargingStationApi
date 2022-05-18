package com.scube.chargingstation.service;

import javax.validation.Valid;

import com.scube.chargingstation.dto.incoming.OtpVerificationIncomingDto;
import com.scube.chargingstation.dto.incoming.UserInfoIncomingDto;

public interface UserInfoService {

	boolean addUserInfo(@Valid UserInfoIncomingDto userInfoIncomingDto);

	boolean moblieOtpVerify(@Valid OtpVerificationIncomingDto otpVerificationIncomingDto);
	
	boolean editUserProfile(UserInfoIncomingDto userInfoIncomingDto);

}
