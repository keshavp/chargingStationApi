package com.scube.chargingstation.dto.incoming;

import lombok.Setter;
import lombok.ToString;
import lombok.Getter;

@Getter
@Setter
@ToString
public class PartnerIncomingDto {
	
	private String id;
	private String partnerName;
	
	private String email;
	private String address1;
	private String address2;
	private String pincode;

	
	private String mobileno;
	private String alternateMobileNo;
	
	private String gstn;
	private String status;
	private double percent;
	private String pymtMode;
	private String bnfName;
	private String beneAccNo;
	private String beneIfsc;
	private String paymentFrequency;
		
}
