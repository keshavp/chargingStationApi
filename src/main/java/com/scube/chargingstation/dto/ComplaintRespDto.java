package com.scube.chargingstation.dto;

import java.time.Instant;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ComplaintRespDto {
	
	private String complaintId;
	
	private String complaintDate;
	
	private String userId;
	private String userName;
	private String userContactNo;
	
	private String complaintCategory;
	private String complaintDetail;
	
	private String complaintStatus;
	
	private Set<CommentsResponseForComplaintsDto> commentsResponse ;
	
	private String commentDate;
	
	private String complaintCloseDate;
	
}
