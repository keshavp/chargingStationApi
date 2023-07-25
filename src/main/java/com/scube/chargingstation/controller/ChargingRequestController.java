package com.scube.chargingstation.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.scube.chargingstation.dto.ChargingHistoryDto;
import com.scube.chargingstation.dto.ChargingPointDto;  
import com.scube.chargingstation.dto.incoming.ChargingPointIncomingDto;
import com.scube.chargingstation.dto.incoming.ChargingRequestDto;
import com.scube.chargingstation.dto.incoming.ChargingStationDto;
import com.scube.chargingstation.dto.incoming.ChargingStationWiseReportIncomingDto;
import com.scube.chargingstation.dto.mapper.ChargingPointMapper;
import com.scube.chargingstation.dto.response.Response;
import com.scube.chargingstation.entity.ChargingPointEntity;
import com.scube.chargingstation.repository.ChargerTypeRepository;
import com.scube.chargingstation.service.ChargingPointService;
import com.scube.chargingstation.service.ChargingRequestService;
import com.scube.chargingstation.util.FileStorageService;

@CrossOrigin(origins = "*", maxAge = 3600)  
@RestController
@RequestMapping(path = {"/api/v1/chargingrequest"}, produces = APPLICATION_JSON_VALUE)
public class ChargingRequestController {

	
	private static final Logger logger = LoggerFactory.getLogger(ChargingRequestController.class);
	
	@Autowired 
	ChargingRequestService chargingRequestService;
	
	@Autowired
	ChargerTypeRepository chargerTypeRepository;
	
	@Autowired
	FileStorageService fileStorageService;
	      
	@Autowired
	ChargingPointService chargingPointService; 	
	
	
	@PostMapping(value ="/getChargingRequestDetails", consumes = APPLICATION_JSON_VALUE)
	public Response getChargingRequestDetails(@RequestBody ChargingRequestDto chargingRequestDto) throws Exception {
		return Response.ok().setPayload(chargingRequestService.getChargingRequestDetails(chargingRequestDto));   
	
	}
	
	@PostMapping(value ="/getChargingHistoryDetailsByStation", consumes = APPLICATION_JSON_VALUE)
	public Response getChargingHistoryDetailsByStation(@RequestBody ChargingStationWiseReportIncomingDto chargingStationWiseReportIncomingDto) throws Exception{
		return Response.ok().setPayload(chargingRequestService.getChargingHistoryDetailsByStation(chargingStationWiseReportIncomingDto));
	}
	
//	@GetMapping( value = "/getAll" )
//	public Response getAllUser() {
//		logger.info("***UserInfoController findAllUsers***");
//		return Response.ok().setPayload(chargingRequestService.changeChargingStatus());
//		
//	}
	
	 	
}