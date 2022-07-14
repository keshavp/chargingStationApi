package com.scube.chargingstation.dto;

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
public class BookingResponseDto {

	private String bookingId;
	
	private String bookingDateAndTime;
	
	private String custName;
	private String custMobileNo;
	private String custVehicleNo;
	
	private String stationId;
	private String stationName;
	
	private String connectorId;
	private String connectorName;
	
	private double bookingAmount;
	private double requestedAmount;
	
	private String bookingStatus;
	
	private String chargeNow;
	
	private String cancelNow;
	
}
