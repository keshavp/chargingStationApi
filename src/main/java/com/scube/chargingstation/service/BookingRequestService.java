package com.scube.chargingstation.service;


import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import com.scube.chargingstation.dto.BookingResponseDto;
import com.scube.chargingstation.dto.BookingSlotsRespDto;
import com.scube.chargingstation.dto.incoming.BookingRequestIncomingDto;
import com.scube.chargingstation.entity.BookingRequestEntity;

public interface BookingRequestService {
	
	public boolean bookNewChargeSlot(@Valid BookingRequestIncomingDto bookingRequestIncomingDto);
	
	List<BookingSlotsRespDto> getAvailableChargingSlotsForChargingPointAndConnector(@Valid BookingRequestIncomingDto bookingRequestIncomingDto);
	
	List<BookingSlotsRespDto> getBookingDatesForChargingPointAndConnector();
	
	List<BookingResponseDto> getBookingHistoryForUserByUserId(String userId);
	
	List<BookingResponseDto> getUpcomingBookingDetailsForUserByUserId(String userId);

	void updateBookingRequestEntityCompletedByChargingRequest(String chargingRequestId);
	
	void bookingAutoCancellationSchedulers();

	public String getUpcomingBookingCancelById(String bookingId) throws Exception;
	
	List<BookingResponseDto> getAllUserPreviousBookingHistoryDetails();
	
	List<BookingResponseDto> getAllUpcomingBookingDetailsInfo();

	
}	
