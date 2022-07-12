package com.scube.chargingstation.service;


import java.util.List;

import javax.validation.Valid;

import com.scube.chargingstation.dto.BookingSlotsRespDto;
import com.scube.chargingstation.dto.incoming.BookingRequestIncomingDto;

public interface BookingRequestService {
	
	public boolean bookNewChargeSlot(@Valid BookingRequestIncomingDto bookingRequestIncomingDto);
	
	List<BookingSlotsRespDto> getAvailableChargingSlotsForChargingPointAndConnector(@Valid BookingRequestIncomingDto bookingRequestIncomingDto);
	
	List<BookingSlotsRespDto> getBookingDatesForChargingPointAndConnector(@Valid BookingRequestIncomingDto bookingRequestIncomingDto);
}	
