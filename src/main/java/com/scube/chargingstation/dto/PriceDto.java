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
public class PriceDto {
	
	private String id;
	private String chargingStationName;
	private String pricingDetailsId;
	private String connectorName;
	
	private double pricingAmount;
	private double chargingAmount;
	private String time;
	private double pricingCgst;
	private double pricingSgst;
	private double pricingKwh;
	private String pricingTime;
	private String pricingType;
	private double cancelBookingAmount;
	
	private String chargingPointId;
	private String connectorId;
}
