package com.scube.chargingstation.dto.incoming;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OtpVerificationIncomingDto {
	
	private String mobilenumber;
	private String otp;
}
