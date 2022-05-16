package com.scube.chargingstation.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scube.chargingstation.dto.incoming.AmenitiesIncomingDto;
import com.scube.chargingstation.dto.response.Response;
import com.scube.chargingstation.service.AmenitiesService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = {"/api/v1/amenities"}, produces = APPLICATION_JSON_VALUE)
public class AmenitiesController {
		
		@Autowired
		AmenitiesService amenitiesService;
		
		private static final Logger logger = LoggerFactory.getLogger(AmenitiesController.class);
		
		// Add Amenities API
		@PostMapping( value = "/addAmenities" , consumes = APPLICATION_JSON_VALUE)
		public Response addAmenities(@Valid @RequestBody AmenitiesIncomingDto amenitiesIncomingDto) {
			logger.info("***AmenitiesController addAmenities***");
		//	logger.info(NEW_ORDER_LOG, createdUser.toString());
			return Response.created().setPayload(amenitiesService.addAmenities(amenitiesIncomingDto));
		}
		
		
		// Edit Amenities API
		@PostMapping( value = "/editAmenities", consumes = APPLICATION_JSON_VALUE)
		public Response editAmenities(@Valid @RequestBody AmenitiesIncomingDto amenitiesIncomingDto) {
			logger.info("***AmenitiesController editAmenities***");
		//	logger.info(NEW_ORDER_LOG, createdUser.toString());
		
			return Response.ok().setPayload(amenitiesService.editAmenities(amenitiesIncomingDto));
			
		}
		
		/*
		// Delete Amenities API
		@PostMapping( value = "/deleteAmenities", consumes = APPLICATION_JSON_VALUE)
			public Response deleteAmenities(@Valid @RequestBody AmenitiesIncomingDto amenitiesIncomingDto) {
				logger.info("***AmenitiesController deleteAmenities***");
					
					return Response.ok().setPayload(amenitiesService.deleteAmenities(amenitiesIncomingDto));
			}   
		*/
		
		
		// Delete Amenities API
		@GetMapping( value = "/deleteAmenities/{id}")
		public Response deleteAmenities(@PathVariable("id") String id) {    
			logger.info("***AmenitiesController deleteAmenities***");
			
			return Response.ok().setPayload(amenitiesService.deleteAmenities(id));    
		}
		
		
		//Get All Amenities API
		@GetMapping (value = "/getAllAmenities")
		public Response getAllAmenities() {
			
			logger.info("***AmenitiesController getAllAmenities***");
			return Response.ok().setPayload(amenitiesService.getAllAmenities());
		}
		
		
		// Get Amenity By ID API
		@GetMapping( value = "/getAmenitiesById/{id}" )
		public Response getAmenitiesById(@PathVariable("id") String id) {
			logger.info("***AmenitiesController getAmenityID***");
		//	logger.info(NEW_ORDER_LOG, createdUser.toString());
			return Response.ok().setPayload(amenitiesService.getAmenitiesById(id));
			
		}
		
		
}