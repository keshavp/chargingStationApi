package com.scube.chargingstation.service;

import javax.validation.Valid;

import com.scube.chargingstation.dto.AuthUserDto;
import com.scube.chargingstation.dto.incoming.ForgetPasswordIncomingDto;
import com.scube.chargingstation.dto.incoming.SetNewPasswordIncomingDto;
import com.scube.chargingstation.dto.incoming.UserLoginIncomingDto;

public interface AuthService {

	AuthUserDto authenticateUser(UserLoginIncomingDto loginRequest);

	boolean resetPassword(String email);
	  
	boolean checkResetPasswordConditions(String email);
	  
	boolean setNewPassword(SetNewPasswordIncomingDto setNewPasswordIncomingDto);
	
	boolean signoutUser(UserLoginIncomingDto loginRequest);

//	boolean generateNewOtp(String mobileNo);
	
	boolean generateNewOtp(ForgetPasswordIncomingDto forgetPasswordIncomingDto);
	
	boolean validateGeneratedOtp(ForgetPasswordIncomingDto forgetPasswordIncomingDto);
	
	void removeOtpNotVerified();

	boolean deleteUserAccount(@Valid SetNewPasswordIncomingDto setNewPasswordIncomingDto);

}
