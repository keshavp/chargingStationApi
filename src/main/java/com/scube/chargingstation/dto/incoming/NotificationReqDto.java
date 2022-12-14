package com.scube.chargingstation.dto.incoming;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NotificationReqDto {

	private String  mobileUser_Id;
	private String  title;
	private String  body;
	private String  sendid;
	
}
