package com.scube.chargingstation.dto;

import java.util.Date;
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
	public class ChargingStatusRespDto  {

		private String id;
		private Double requestedAmount;
		private Double requestedKwh;
		private Double currentKwh;
		private Double chargingPercent;
		private Double chargingSpeed;
		private String estimatedTime;
		private String vehicleNo;
		
		//requestedAmount, requestedKwh, chargingSpeed, currentKwh, chargingPercent, vehicleNo, estimatedTime
		
}
