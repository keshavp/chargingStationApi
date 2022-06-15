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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.net.MediaType;
import com.scube.chargingstation.dto.response.Response;
import com.scube.chargingstation.service.ConnectorService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = {"/api/v1/conector"}, produces = APPLICATION_JSON_VALUE)
public class ConnectorController {
	
	private static final Logger logger = LoggerFactory.getLogger(ConnectorController.class);
	
	
	  @Autowired ConnectorService connectorService;
	  
	  @GetMapping( value = "/deleteConnector/{id}" ) 
	  public Response deleteConnector(@PathVariable("id") String id) {
		  logger.info("***ConnectorController deleteConnector***");
		  return Response.ok().setPayload(connectorService.deleteConnector(id)); 
	  }

}