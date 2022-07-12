package com.scube.chargingstation.dto.incoming;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter
public class BookingRequestIncomingDto {
	
	private String chargingPointId;
	private String chargingPointName;
	
	private String connectorId;
	private String connectorName;
	
	private String userContactNo;
	
	private double requestedBookingAmount;
	
	private String bookingStatus;
	
	private String requestedBookingDate;
	
	private String custName;
	
	private String custMobileNo;
	
	private String custVehicleNo;
		
}
