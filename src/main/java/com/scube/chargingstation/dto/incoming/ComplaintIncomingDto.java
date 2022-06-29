package com.scube.chargingstation.dto.incoming;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComplaintIncomingDto {
	
	private String complaintId;
	
//	private String userMobileNo;
	private String userName;
	private String userContactNo;
	
	private String complaintCategory;
	private String complaintDetail;
	
	private String complaintStatus;
	
	private String complaintRemark;
	
}
