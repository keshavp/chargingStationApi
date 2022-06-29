package com.scube.chargingstation.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComplaintResponseCommentsDto {
	
	private String complaintId;
	
	private String comments;
	
	private String userMobileNo;
}
