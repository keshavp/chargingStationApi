package com.scube.chargingstation.service;

import com.scube.chargingstation.dto.incoming.OtpVerificationIncomingDto;
import com.scube.chargingstation.entity.UserInfoOtpEntity;

public interface UserInfoOtpService {

	void insertOtpDate(UserInfoOtpEntity userInfoOtpEntity);
	boolean moblieOtpVerify(OtpVerificationIncomingDto otpVerificationIncomingDto);
	void removeUnVerificationUser();
}
