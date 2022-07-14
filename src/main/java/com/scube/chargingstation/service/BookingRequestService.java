package com.scube.chargingstation.service;


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
<<<<<<< Updated upstream
<<<<<<< Updated upstream

	void updateBookingRequestEntityCompletedByChargingRequest(String chargingRequestId);
	
	void bookingAutoCancellationSchedulers();
	
=======
=======
>>>>>>> Stashed changes
	
	List<BookingResponseDto> getAllUserPreviousBookingHistoryDetails();
	
	List<BookingResponseDto> getAllUpcomingBookingDetailsInfo();
<<<<<<< Updated upstream
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
	
}	
