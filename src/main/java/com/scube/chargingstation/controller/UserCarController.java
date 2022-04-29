package com.scube.chargingstation.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scube.chargingstation.dto.incoming.ChargingRequestDto;
import com.scube.chargingstation.dto.incoming.ChargingStationDto;
import com.scube.chargingstation.dto.incoming.UserCarDto;
import com.scube.chargingstation.dto.response.Response;
import com.scube.chargingstation.entity.UserInfoEntity;
import com.scube.chargingstation.exception.BRSException;
import com.scube.chargingstation.repository.UserInfoRepository;
import com.scube.chargingstation.service.UserCarService;
import com.scube.chargingstation.service.ChargingRequestService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = {"/api/v1/usercar"}, produces = APPLICATION_JSON_VALUE)
public class UserCarController {
	
	private static final Logger logger = LoggerFactory.getLogger(UserCarController.class);

	@Autowired 
	UserCarService userCarService;
	
	@Autowired
	UserInfoRepository userInfoRepository;
	
	@PostMapping(value ="/addUserCars", consumes = APPLICATION_JSON_VALUE)
	public Response addUserCars(@Valid @RequestBody UserCarDto userCarDto)  {
		
		logger.info("***addUserCars***");
		return Response.ok().setPayload(userCarService.addUserCars(userCarDto));
		
	}
	
	
	/*
	 * @GetMapping("/getCType/{id}") public ResponseEntity<byte[]>
	 * getFileFromStorageSelection(@PathVariable Long id ) throws Exception {
	 */
	
	
	@GetMapping(value ="/getUserCars/{mobileUser_Id}", consumes = APPLICATION_JSON_VALUE)
	public Response getUserCars(@PathVariable String mobileUser_Id)  {
		
		UserInfoEntity userInfoEntity = userInfoRepository.findByMobilenumber(mobileUser_Id);
		if(userInfoEntity==null)
		{
			throw BRSException.throwException("Error: User does not exist"); 
		}
		
		logger.info("***getUserCars***");
		return Response.ok().setPayload(userCarService.getUserCars(userInfoEntity));
		
	}
	
}
