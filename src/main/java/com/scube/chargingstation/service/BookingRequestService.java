package com.scube.chargingstation.service;

import java.util.List;

import javax.validation.Valid;

import com.scube.chargingstation.dto.BookingResponseDto;
import com.scube.chargingstation.dto.BookingSlotsRespDto;
import com.scube.chargingstation.dto.ChargingRequestRespDto;
import com.scube.chargingstation.dto.incoming.BookingRequestIncomingDto;
import com.scube.chargingstation.dto.incoming.ChargingStationWiseReportIncomingDto;
import com.scube.chargingstation.dto.incoming.UserPreviousBookingHistoryDetailsIncomingDto;
import com.scube.chargingstation.entity.BookingRequestEntity;

public interface BookingRequestService {
	
	public boolean bookNewChargeSlot(@Valid BookingRequestIncomingDto bookingRequestIncomingDto);
	
	List<BookingSlotsRespDto> getAvailableChargingSlotsForChargingPointAndConnector(@Valid BookingRequestIncomingDto bookingRequestIncomingDto);
	
	List<BookingSlotsRespDto> getBookingDatesForChargingPointAndConnector();
	
	List<BookingResponseDto> getBookingHistoryForUserByUserId(String userId);
	
	List<BookingResponseDto> getUpcomingBookingDetailsForUserByUserId(String userId);

	void updateBookingRequestEntityCompletedByChargingRequest(String chargingRequestId);
	
	public void bookingAutoCancellationSchedulers() throws Exception;

	public String getUpcomingBookingCancelById(String bookingId) throws Exception;
	
	//List<BookingResponseDto> getAllUserPreviousBookingHistoryDetails();
	List<BookingResponseDto> getAllUserPreviousBookingHistoryDetailsByRole(UserPreviousBookingHistoryDetailsIncomingDto userPreviousBookingHistoryDetailsIncomingDto);
	
	List<BookingResponseDto> getAllUpcomingBookingDetailsInfoByRole(UserPreviousBookingHistoryDetailsIncomingDto userPreviousBookingHistoryDetailsIncomingDto);

	public void oneDayBeforeBookingReminderSchedulers();

	public void oneHourBeforeBookingReminderSchedulers();

	public boolean bookingReminderById(String bookingId);
	
	boolean getBookingRequestDetailsByConnectorId(String chargingPointId, String connectorId);
	
	
	
}	
