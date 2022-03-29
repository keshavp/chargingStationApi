package com.scube.chargingstation.service;

import com.scube.chargingstation.dto.AuthUserDto;
import com.scube.chargingstation.dto.incoming.SetNewPasswordIncomingDto;
import com.scube.chargingstation.dto.incoming.UserLoginIncomingDto;

public interface AuthService {

	AuthUserDto authenticateUser(UserLoginIncomingDto loginRequest);

	boolean resetPassword(String email);
	  
	boolean checkResetPasswordConditions(String email);
	  
	boolean setNewPassword(SetNewPasswordIncomingDto setNewPasswordIncomingDto);

}
