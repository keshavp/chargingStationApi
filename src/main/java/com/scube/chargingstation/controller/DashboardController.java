package com.scube.chargingstation.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scube.chargingstation.dto.response.Response;
import com.scube.chargingstation.service.DashboardService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = {"/api/v1/dashboard"}, produces = APPLICATION_JSON_VALUE)
public class DashboardController {

	@Autowired
	DashboardService dashboardService;
	
	private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);
	
	
	@GetMapping( value = "/aDashboard")
	public Response getAdminDashboard() {
		logger.info("***DashboardController getAdminDashboard***");
		
		return Response.ok().setPayload(dashboardService.getAdminDashboard());    
	}
	
	@GetMapping( value = "/pDashboard/{id}")
	public Response getPartnerDashboard(@PathVariable("id") String id) {    
		logger.info("***DashboardController getPartnerDashboard***");
		
		return Response.ok().setPayload(dashboardService.getPartnerDashboardById(id));    
	}
	
	@GetMapping( value = "/uDashboard/{id}")
	public Response getUserDashboard(@PathVariable("id") String id) {    
		logger.info("***DashboardController getUserDashboard***");
		
		return Response.ok().setPayload(dashboardService.getUserDashboardById(id));    
	}
}
