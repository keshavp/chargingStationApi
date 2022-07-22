package com.scube.chargingstation.dto;

import java.time.Instant;
import java.util.Date;
import java.util.Set;

import javax.print.DocFlavor.STRING;

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
	public class ChargingRequestRespDto  {

		private Double requestedAmount;
		private Double requestedKwh;
		private String vehicleNo;
		private String chargePoint;
		private String chargePointAddr;
		private String connector;
		private String chargingTime;
		private String chargedKwh;
		private String ActualAmt;
		private String chargePointName;
		private String name;
		
		private String startTime;
		private String  stopTime;
		
		private String custName;
		private String mobileNo;
		private String partnerName;
		private String  invoiceFilePath;
		
	//	private String chargingTimeForUser;
		
		
		//requestedAmount, requestedKwh, chargingSpeed, currentKwh, chargingPercent, vehicleNo, estimatedTime
		
}
