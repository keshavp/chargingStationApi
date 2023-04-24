package com.scube.chargingstation.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scube.chargingstation.dto.incoming.DailyUserWalletreportIncomingDto;
import com.scube.chargingstation.dto.response.Response;
import com.scube.chargingstation.service.DailyUserWalletService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = {"/api/v1/dailyReport"}, produces = APPLICATION_JSON_VALUE)
public class DailyUserWalletReprtController {
	
	
	private static final Logger logger = LoggerFactory.getLogger(DailyUserWalletReprtController.class);
	
	@Autowired
	DailyUserWalletService dailyUserWalletService;
	

	
	@PostMapping(value = "/getuserwalletreport", consumes = APPLICATION_JSON_VALUE)
	public Response getDailyEmpWallets(@Valid @RequestBody DailyUserWalletreportIncomingDto dailyUserWalletreportIncomingDto) {
	    logger.info("***getuserwalletreprt***" + dailyUserWalletreportIncomingDto.getLatestDate());

	    return Response.ok().setPayload(dailyUserWalletService.getDailyEmpWallets(dailyUserWalletreportIncomingDto));
	}
	
}
