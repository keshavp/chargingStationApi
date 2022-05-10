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
import com.scube.chargingstation.service.ChargerTypeService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = {"/api/v1/chargerType"}, produces = APPLICATION_JSON_VALUE)
public class ChargerTypeController {

	@Autowired
	ChargerTypeService chargertypeservice;
	private static final Logger logger = LoggerFactory.getLogger(ChargerTypeController.class);
	
	@GetMapping( value = "/activeChargerType" )
	public Response findActiveChargerType() {
     logger.info("***RoleController findActiveChargerType***"); 
     return Response.ok().setPayload(chargertypeservice.findActiveChargerType());
	}
	
}
