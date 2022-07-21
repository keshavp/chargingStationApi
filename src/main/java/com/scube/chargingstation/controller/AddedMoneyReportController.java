package com.scube.chargingstation.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scube.chargingstation.dto.incoming.AddedMoneyWalletRequestDto;
import com.scube.chargingstation.dto.response.Response;
import com.scube.chargingstation.service.AddedMoneyReportService;

@CrossOrigin (origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = {"/api/v1/addedMoneyReport"}, produces = APPLICATION_JSON_VALUE)
public class AddedMoneyReportController {
	
	private static final Logger logger = LoggerFactory.getLogger(AddedMoneyReportController.class);
	
	@Autowired
	AddedMoneyReportService addedMoneyReportService;
	
	// Get Added Money In Wallet Details By Date  ---> For Admin
	@PostMapping (value = "/getAddedMoneyToWalletDetailsByDateForAllUsers", consumes = APPLICATION_JSON_VALUE)
	public Response getAddedMoneyToWalletDetailsByUserId(@RequestBody AddedMoneyWalletRequestDto addedMoneyWalletRequestDto) {
		
		logger.info("***BookingRequestController getAddedMoneyToWalletDetailsByDateForUserId***");
		return Response.ok().setPayload(addedMoneyReportService.getAddedMoneyToWalletDetailsByUserId(addedMoneyWalletRequestDto));	
	}
	
	
	
	
}
