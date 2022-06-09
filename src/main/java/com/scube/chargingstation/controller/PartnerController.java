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

import com.scube.chargingstation.dto.incoming.PartnerIncomingDto;
import com.scube.chargingstation.dto.response.Response;
import com.scube.chargingstation.service.PartnerService;


@CrossOrigin (origins = "*", maxAge = 3600)
@RestController
@RequestMapping (path = {"api/v1/partners"}, produces = APPLICATION_JSON_VALUE)
public class PartnerController {
	
	@Autowired
	PartnerService partnerService;
	
	
	private static final Logger Logger = LoggerFactory.getLogger(PartnerController.class);
	
	@PostMapping ( value = "/addPartner", consumes = APPLICATION_JSON_VALUE)
	public Response addPartner(@Valid @RequestBody PartnerIncomingDto partnerIncomingDto) {
		Logger.info("***PartnerController addPartners***");
		return Response.created().setPayload(partnerService.addPartner(partnerIncomingDto));
	}
	
	
	@PostMapping ( value = "/editPartner", consumes = APPLICATION_JSON_VALUE)
	public Response editPartner(@Valid @RequestBody PartnerIncomingDto partnerIncomingDto) {
		Logger.info("***PartnerController editPartner***");
		return Response.ok().setPayload(partnerService.editPartner(partnerIncomingDto));
				
	}
	
	@GetMapping ( value = "/getAllPartners")
	public Response getAllPartners() {
		Logger.info("***PartnerController getAllPartners***");
		return Response.ok().setPayload(partnerService.getAllPartners());
	}
	
	@GetMapping ( value = "/getPartnerUserById/{id}")
	public Response getPartnerUserById(@PathVariable String id) {
		Logger.info("***PartnerController getPartnerUserById***");
		return Response.ok().setPayload(partnerService.getPartnerUserById(id));
		
	}
	
	@GetMapping ( value = "/getAllActivePartners")
	public Response getAllActivePartners() {
		Logger.info("***PartnerController getAllActivePartners***");
		return Response.ok().setPayload(partnerService.getAllActivePartners());
	}
	
	
}
