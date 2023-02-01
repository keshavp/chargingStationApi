package com.scube.chargingstation.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scube.chargingstation.dto.incoming.ServerAPIResetIncomingDto;
import com.scube.chargingstation.dto.response.Response;
import com.scube.chargingstation.service.ServerAPIResetService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CrossOrigin
@RestController
@RequestMapping (path={"/api/v1/serverReset"}, produces = APPLICATION_JSON_VALUE)
public class ServerAPResetController {
	
	@Autowired
	ServerAPIResetService serverAPIResetService;
	
	private static final Logger logger = LoggerFactory.getLogger(ServerAPResetController.class);
	
	
	@PostMapping(value = "/hardReset", consumes = APPLICATION_JSON_VALUE)
	public Response hardReset(@Valid @RequestBody ServerAPIResetIncomingDto serverAPIResetIncomingDto) {
		
		logger.info("---------- ServerAPResetController hardReset -----------------");
		return Response.ok().setPayload(serverAPIResetService.hardReset(serverAPIResetIncomingDto));
	}
	
	
	@PostMapping(value = "/softReset", consumes = APPLICATION_JSON_VALUE)
	public Response softReset(@Valid @RequestBody ServerAPIResetIncomingDto serverAPIResetIncomingDto) {
		
		logger.info("---------- ServerAPResetController softReset -----------------");
		return Response.ok().setPayload(serverAPIResetService.softReset(serverAPIResetIncomingDto));
	}
	
	
	@GetMapping(value = "/sendEmailForChargerStatus")
	public Response sendEmailForChargerStatus() {
		
		logger.info("---------- ServerAPResetController sendEmailForChargerStatus -----------------");
		return Response.ok().setPayload(serverAPIResetService.sendEmailForChargerStatus());
		
	}
	
	
	@PostMapping(value = "/saveAllChargerStatusData")
	public Response saveAllChargerStatusData() {
		
		logger.info("---------- ServerAPResetController saveAllChargerStatusData -----------------");
		return Response.ok().setPayload(serverAPIResetService.saveAllChargerStatusData());
		
	}
	
	
	@PostMapping(value = "/getAllChargerStatusDataReportByDateRange")
	public Response getAllChargerStatusDataReportByDateRange(@Valid @RequestBody ServerAPIResetIncomingDto serverAPIResetIncomingDto) {
		
		logger.info("---------- ServerAPResetController getAllChargerStatusDataReport -----------------");
		return Response.ok().setPayload(serverAPIResetService.getAllChargerStatusDataReportByDateRange(serverAPIResetIncomingDto));
		
	}

}
