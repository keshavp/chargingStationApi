package com.scube.chargingstation.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scube.chargingstation.dto.ChargingPointConnectorRateDto;
import com.scube.chargingstation.dto.incoming.ChargingPointConnectorRateIncomingDto;
import com.scube.chargingstation.dto.response.Response;
import com.scube.chargingstation.service.PriceService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = {"/api/v1/PriceMaster"}, produces = APPLICATION_JSON_VALUE)

public class PriceMasterController {
	@Autowired
	PriceService priceservice;
	 static final Logger logger = LoggerFactory.getLogger(PriceMasterController.class);
	 
	 @PostMapping( value = "/addPrice" , consumes = APPLICATION_JSON_VALUE)	
	 public Response addPrice(@Valid @RequestBody ChargingPointConnectorRateIncomingDto chargingPointConnectorRateIncomingDto)
		{
			return Response.created().setPayload(priceservice.addPrice(chargingPointConnectorRateIncomingDto));
			 
		}
		
	 @PostMapping( value = "/addPriceRate" , consumes = APPLICATION_JSON_VALUE)	
	 public Response addPriceRate(@Valid @RequestBody ChargingPointConnectorRateIncomingDto chargingPointConnectorRateIncomingDto)
		{
			return Response.created().setPayload(priceservice.addPriceRate(chargingPointConnectorRateIncomingDto));
			 
		}
		
	 
	 @PostMapping( value = "/editPriceRate" , consumes = APPLICATION_JSON_VALUE)	
	 public Response editPriceRate(@Valid @RequestBody ChargingPointConnectorRateIncomingDto chargingPointConnectorRateIncomingDto)
		{
			return Response.created().setPayload(priceservice.editPriceRate(chargingPointConnectorRateIncomingDto));
			 
		}
	

		

}
