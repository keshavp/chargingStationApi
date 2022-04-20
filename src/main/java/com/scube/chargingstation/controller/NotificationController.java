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

import com.scube.chargingstation.dto.incoming.ChargingRequestDto;
import com.scube.chargingstation.dto.incoming.ChargingStationDto;
import com.scube.chargingstation.dto.incoming.NotificationReqDto;
import com.scube.chargingstation.dto.incoming.UserInfoIncomingDto;
import com.scube.chargingstation.dto.incoming.UserWalletRequestDto;
import com.scube.chargingstation.dto.response.Response;
import com.scube.chargingstation.service.ChargingRequestService;
import com.scube.chargingstation.service.NotificationService;
import com.scube.chargingstation.service.UserPaymentService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = {"/api/v1/notification"}, produces = APPLICATION_JSON_VALUE)
public class NotificationController {

	
	private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);
	
	@Autowired 
	NotificationService notificationService;
	

	
	@PostMapping( value = "/sendNotification" , consumes = APPLICATION_JSON_VALUE)
	public Response sendNotification(@Valid @RequestBody NotificationReqDto notificationReqDto) {
		
		logger.info("***sendNotification***");
		
				return Response.ok().setPayload(notificationService.sendNotification(notificationReqDto));
		
	}
	
}
