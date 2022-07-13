package com.scube.chargingstation.service;


import java.util.List;

import javax.validation.Valid;

import com.scube.chargingstation.dto.BookingResponseDto;
import com.scube.chargingstation.dto.BookingSlotsRespDto;
import com.scube.chargingstation.dto.incoming.BookingRequestIncomingDto;

public interface BookingRequestService {
	
	public boolean bookNewChargeSlot(@Valid BookingRequestIncomingDto bookingRequestIncomingDto);
	
	List<BookingSlotsRespDto> getAvailableChargingSlotsForChargingPointAndConnector(@Valid BookingRequestIncomingDto bookingRequestIncomingDto);
	
	List<BookingSlotsRespDto> getBookingDatesForChargingPointAndConnector();
	
	List<BookingResponseDto> getBookingHistoryForUserByUserId(String userId);
	
	List<BookingResponseDto> getUpcomingBookingDetailsForUserByUserId(String userId);
}	
