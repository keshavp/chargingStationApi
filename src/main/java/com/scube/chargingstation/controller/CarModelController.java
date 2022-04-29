package com.scube.chargingstation.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scube.chargingstation.dto.response.Response;
import com.scube.chargingstation.service.CarModelService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = {"/api/v1/carmodel"}, produces = APPLICATION_JSON_VALUE)
public class CarModelController {
	
	
	@Autowired 
	CarModelService carModelService;
	
	@GetMapping(value ="/getCarModels", consumes = APPLICATION_JSON_VALUE)
	public Response getCarModels() throws Exception {
		
		return Response.ok().setPayload(carModelService.getCarModels());
	}
}
