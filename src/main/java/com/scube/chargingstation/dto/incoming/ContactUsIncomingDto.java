package com.scube.chargingstation.dto.incoming;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

public class ContactUsIncomingDto {

	private String name;
	private String email;
	private String message;
	private String mobile_no;
	

}
