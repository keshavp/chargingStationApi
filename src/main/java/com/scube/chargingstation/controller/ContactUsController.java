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

import com.scube.chargingstation.dto.incoming.ContactUsIncomingDto;
import com.scube.chargingstation.dto.response.Response;
import com.scube.chargingstation.service.ContactUsService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = {"/api/v1/ContactUs"}, produces = APPLICATION_JSON_VALUE)


public class ContactUsController {

	@Autowired
	ContactUsService contactUsService;
	
	private static final Logger logger = LoggerFactory.getLogger(ContactUsController.class);
	
	@PostMapping( value = "/getContactUs" , consumes = APPLICATION_JSON_VALUE)
	public Response getContactUs (@Valid @RequestBody ContactUsIncomingDto contactUsIncomingDto)
	{
		
		logger.info("***ContactUsController getContactUs***");
		return Response.created().setPayload(contactUsService.getContactUs(contactUsIncomingDto));
		
	}
	
	
	@PostMapping( value = "/mailInformation2" , consumes = APPLICATION_JSON_VALUE)
	public Response sendEmail(@Valid @RequestBody ContactUsIncomingDto contactUsIncomingDto) throws  Exception
	{
		logger.info("***ContactUsController mailservice***");
		
		return Response.ok().setPayload(contactUsService.sendEmail(contactUsIncomingDto));		
		
	}
	
	
}
