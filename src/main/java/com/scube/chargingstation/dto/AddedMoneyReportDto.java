package com.scube.chargingstation.dto;

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
public class AddedMoneyReportDto {
	
	private String userName;
	private String userMobileNo;
	
	private double addedAmount;
	
	private String paymentDate;
	
	private String orderId;
	
	private String transactionId;
	private String transactionActivity;
	private String transactionType;
	
}
