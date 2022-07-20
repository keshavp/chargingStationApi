package com.scube.chargingstation.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scube.chargingstation.dto.incoming.ChargingStationWiseReportIncomingDto;
import com.scube.chargingstation.dto.response.Response;
import com.scube.chargingstation.service.ChargingPointService;
import com.scube.chargingstation.service.ReportService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = {"/api/v1/report"}, produces = APPLICATION_JSON_VALUE)
public class ReportController {

	@Autowired
	ReportService	reportService;
	
	@PostMapping(value ="/getChargingDetailsForAllStations", consumes = APPLICATION_JSON_VALUE)
	public Response getChargingDetailsForAllStations(@RequestBody ChargingStationWiseReportIncomingDto chargingStationWiseReportIncomingDto) throws Exception{
		
		return Response.ok().setPayload(reportService.getChargingDetailsForAllStations(chargingStationWiseReportIncomingDto));
	}
}
