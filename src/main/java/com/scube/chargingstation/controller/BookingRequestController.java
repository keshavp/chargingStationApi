package com.scube.chargingstation.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.ThrowsAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scube.chargingstation.dto.incoming.BookingRequestIncomingDto;
import com.scube.chargingstation.dto.incoming.ChargingStationWiseReportIncomingDto;
import com.scube.chargingstation.dto.incoming.UserPreviousBookingHistoryDetailsIncomingDto;
//import com.scube.chargingstation.dto.incoming.BookingSlotsDummyIncomingDto;
import com.scube.chargingstation.dto.response.Response;
import com.scube.chargingstation.service.BookingRequestService;

@CrossOrigin (origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = {"/api/v1/bookingRequest"}, produces = APPLICATION_JSON_VALUE)
public class BookingRequestController {
	
	private static final Logger logger = LoggerFactory.getLogger(BookingRequestController.class);
	
	@Autowired
	BookingRequestService bookingRequestService;
	
	// Book New Charge Slot - For User   
	@PostMapping (value = "/bookNewSlot", consumes = APPLICATION_JSON_VALUE)
	public Response bookSlotForCharging(@RequestBody BookingRequestIncomingDto bookingRequestIncomingDto) {
		
		logger.info("***BookingRequestController bookSlotForCharging***");
		return Response.created().setPayload(bookingRequestService.bookNewChargeSlot(bookingRequestIncomingDto));
		
	}
	
	
	@PostMapping (value = "/getAvailableChargingSlotsForChargingPointAndConnector", consumes = APPLICATION_JSON_VALUE)
	public Response getAvailableChargingSlotsForChargingPointAndConnector(@RequestBody BookingRequestIncomingDto bookingRequestIncomingDto) throws Exception {
		
		logger.info("***BookingRequestController getAvailableChargingSlots***");
		return Response.ok().setPayload(bookingRequestService.getAvailableChargingSlotsForChargingPointAndConnector(bookingRequestIncomingDto));
		
	}
	
	
	@GetMapping (value = "/getBookingDatesForChargingPointAndConnector")
	public Response getBookingDatesForChargingPointAndConnector() throws Exception {
		
		logger.info("***BookingRequestController getBookingDatesForChargingPointAndConnector***");
		return Response.ok().setPayload(bookingRequestService.getBookingDatesForChargingPointAndConnector());
	}
	
	// Get Previous Booking History
	@GetMapping (value = "/getBookingHistoryForUserByUserId/{userMobileNo}")
	public Response getBookingHistoryForUserByUserId(@PathVariable ("userMobileNo") String userMobileNo) {
		
		logger.info("***BookingRequestController getBookingHistoryForUserByUserId***");
		return Response.ok().setPayload(bookingRequestService.getBookingHistoryForUserByUserId(userMobileNo));	
	}
	
	// Get Upcoming Booking Info
	@GetMapping (value = "/getUpcomingBookingInfoForUserByUserId/{userMobileNo}")
	public Response getUpcomingBookingDetailsForUserByUserId(@PathVariable ("userMobileNo") String userMobileNo) {
		
		logger.info("***BookingRequestController getUpcomingBookingDetailsForUserByUserId***");
		return Response.ok().setPayload(bookingRequestService.getUpcomingBookingDetailsForUserByUserId(userMobileNo));
		
	}
	
	
	// Get All Previous Booking History
	/*@GetMapping (value = "/getAllUserPreviousBookingHistoryDetails")
	public Response getAllBookingHistoryDetails() {
		
		logger.info("***BookingRequestController getAllUserPreviousBookingHistoryDetails***");
		return Response.ok().setPayload(bookingRequestService.getAllUserPreviousBookingHistoryDetails());
		
	}*/
	
	@PostMapping(value ="/getAllUserPreviousBookingHistoryDetailsByRole", consumes = APPLICATION_JSON_VALUE)
	public Response getChargingHistoryDetailsByStation(@RequestBody UserPreviousBookingHistoryDetailsIncomingDto userPreviousBookingHistoryDetailsIncomingDto) throws Exception{
		logger.info("***BookingRequestController getAllUserPreviousBookingHistoryDetailsByRole***");
		return Response.ok().setPayload(bookingRequestService.getAllUserPreviousBookingHistoryDetailsByRole(userPreviousBookingHistoryDetailsIncomingDto));
	}
	
	
	// Get All Upcoming Booking History
	@PostMapping (value = "/getAllUpcomingBookingDetailsInfoByRole", consumes = APPLICATION_JSON_VALUE)
	public Response getAllUpcomingBookingDetailsInfoByRole(@RequestBody UserPreviousBookingHistoryDetailsIncomingDto userPreviousBookingHistoryDetailsIncomingDto)throws Exception{
		
		logger.info("***BookingRequestController getAllUpcomingBookingDetailsInfoByRole***");
		return Response.ok().setPayload(bookingRequestService.getAllUpcomingBookingDetailsInfoByRole(userPreviousBookingHistoryDetailsIncomingDto));
		
	}

	// Get Upcoming Booking Info
	@GetMapping (value = "/upcomingBookingCancelById/{bookingId}")
	public Response upcomingBookingCancelByUserMobileNo(@PathVariable ("bookingId") String bookingId) throws Exception {
		
		logger.info("***BookingRequestController getUpcomingBookingDetailsForUserByUserId***");
		return Response.ok().setPayload(bookingRequestService.getUpcomingBookingCancelById(bookingId));
		
	}
	
	@GetMapping (value = "/bookingReminderById/{bookingId}")
	public Response bookingReminderById(@PathVariable ("bookingId") String bookingId) throws Exception {
		
		logger.info("***BookingRequestController bookingReminderById***");
		return Response.ok().setPayload(bookingRequestService.bookingReminderById(bookingId));
		
	}
	
	
}
