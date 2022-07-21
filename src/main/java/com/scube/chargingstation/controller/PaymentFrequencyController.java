package com.scube.chargingstation.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scube.chargingstation.dto.response.Response;
import com.scube.chargingstation.service.PaymentFrequencyService;

@CrossOrigin (origins = "*", maxAge = 3600)
@RestController
@RequestMapping (path = {"api/v1/paymentFrequency"}, produces = APPLICATION_JSON_VALUE)
public class PaymentFrequencyController {

	private static final Logger logger = LoggerFactory.getLogger(PaymentFrequencyController.class);
	
	@Autowired
	PaymentFrequencyService frequencyService;
	
	
	@GetMapping( value = "/getAllActive" )
	public Response findAllPaymentFrequency() {
		logger.info("***PaymentFrequencyController findAllPaymentFrequency***");
	//	logger.info(NEW_ORDER_LOG, createdUser.toString());
		return Response.ok().setPayload(frequencyService.findAllActive());
		
	}
}
