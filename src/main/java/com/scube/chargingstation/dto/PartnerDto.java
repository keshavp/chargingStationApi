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
public class PartnerDto {
	
	private String partnerName;
	private String id;
	private String address1;
	private String address2;
	private String pincode;
	
	private String email;
	private String mobileno;
	private String alternateMobileNo;
	
	private String status;
	private String gstn;
	
	private double percent;
	private String pymtMode;
	private String bnfName;
	private String beneAccNo;
	private String beneIfsc;
	private String paymentFrequency;
	
	
}
