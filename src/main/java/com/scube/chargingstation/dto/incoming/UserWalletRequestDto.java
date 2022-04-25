package com.scube.chargingstation.dto.incoming;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserWalletRequestDto{

	private String  chargeRequestId;
	private String  mobileUser_Id;
	private String  requestAmount;
	private String  transactionType;
	private String  transactionId;
	private String  orderId;
	private String  razorSignature;
	
	
	
}