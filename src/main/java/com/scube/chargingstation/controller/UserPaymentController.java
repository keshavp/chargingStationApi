package com.scube.chargingstation.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scube.chargingstation.dto.AuthUserDto;
import com.scube.chargingstation.dto.ChargingHistoryDto;
import com.scube.chargingstation.dto.ChargingPointDto;
import com.scube.chargingstation.dto.RazorOrderIdDto;
import com.scube.chargingstation.dto.incoming.ChargingRequestDto;
import com.scube.chargingstation.dto.incoming.ChargingStationDto;
import com.scube.chargingstation.dto.incoming.UserInfoIncomingDto;
import com.scube.chargingstation.dto.incoming.UserWalletRequestDto;
import com.scube.chargingstation.dto.mapper.ChargingPointMapper;
import com.scube.chargingstation.dto.response.Response;
import com.scube.chargingstation.entity.ChargingPointEntity;
import com.scube.chargingstation.service.ChargingRequestService;
import com.scube.chargingstation.service.UserPaymentService;

import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = {"/api/v1/userpayment"}, produces = APPLICATION_JSON_VALUE)
public class UserPaymentController {

	
	private static final Logger logger = LoggerFactory.getLogger(UserPaymentController.class);
	
	@Autowired 
	UserPaymentService userPaymentService;
	
   
	
	@PostMapping( value = "/processWalletMoney" , consumes = APPLICATION_JSON_VALUE)
	public Response processWalletMoney(@Valid @RequestBody UserWalletRequestDto userWalletRequestDto) {
		logger.info("***processWalletMoney***");
		
				return Response.ok().setPayload(userPaymentService.processWalletMoney(userWalletRequestDto));
		
	}
	
	
	@PostMapping( value = "/getMyWalletBalance" , consumes = APPLICATION_JSON_VALUE)
	public Response getMyWalletBalance(@Valid @RequestBody UserWalletRequestDto userWalletRequestDto) {
		logger.info("***getMyWalletBalance***");
		
				return Response.ok().setPayload(userPaymentService.getMyWalletBalance(userWalletRequestDto));
		
	}
	
	@PostMapping( value = "/addWalletMoneyRequest" , consumes = APPLICATION_JSON_VALUE)
	public Response addWalletMoneyRequest(@Valid @RequestBody UserWalletRequestDto userWalletRequestDto) {
		logger.info("***addWalletMoneyRequest***");
		
		RazorOrderIdDto responseData;
 		 
 		 responseData =	userPaymentService.addWalletMoneyRequest(userWalletRequestDto);
		
		return Response.ok().setPayload(responseData);
		
	}
	
	@PostMapping( value = "/addWalletMoneyTransaction" , consumes = APPLICATION_JSON_VALUE)
	public Response addWalletMoneyTransaction(@Valid @RequestBody UserWalletRequestDto userWalletRequestDto)  {
		logger.info("***addWalletMoneyTransaction***");
		
		return Response.ok().setPayload(userPaymentService.addWalletMoneyTransaction(userWalletRequestDto));
		
	}
	
	@PostMapping(value ="/getChargingTrHistory", consumes = APPLICATION_JSON_VALUE)
	public Response getChargingTrHistory(@Valid @RequestBody UserWalletRequestDto userWalletRequestDto) {
		// TODO Auto-generated method stub
		logger.info("***getChargingTrHistory***"+userWalletRequestDto.getMobileUser_Id());
		return Response.ok().setPayload(userPaymentService.getChargingTrHistory(userWalletRequestDto));
		
	}
	
	@PostMapping( value = "/initiateAvenueTransaction" , consumes = APPLICATION_JSON_VALUE)
	public Response initiateAvenueTransaction(@Valid @RequestBody UserWalletRequestDto userWalletRequestDto)  {
		logger.info("***addWalletMoneyTransaction***");
		
		return Response.ok().setPayload(userPaymentService.initiateAvenueTransaction(userWalletRequestDto));
		
	}
	
}
