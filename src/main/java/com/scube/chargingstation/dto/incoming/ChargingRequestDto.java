package com.scube.chargingstation.dto.incoming;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ChargingRequestDto{

	private String  chargePointId;
	private int  connectorId;
	private String  mobileUser_Id;
	private String  status;
	private String  requestAmount;
	
	private String  name;
	private String  mobileNo;
	private String  vechicleNo;
	
}