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
import com.scube.chargingstation.dto.incoming.ConnectorTypeIncomingDto;
import com.scube.chargingstation.dto.response.Response;
import com.scube.chargingstation.service.ConnectorTypeService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = {"/api/v1/conectortype"}, produces = APPLICATION_JSON_VALUE)
public class ConnectorTypeController {
	
	@Autowired
	ConnectorTypeService connectorTypeService;
	
	private static final Logger logger = LoggerFactory.getLogger(ConnectorTypeController.class);
	@GetMapping(value ="/getConnectors")
	public Response getConnectors()throws Exception
	{
		return Response.ok().setPayload(connectorTypeService.getConnectors());
	}
	@PostMapping( value = "/addConnectorType" , consumes = APPLICATION_JSON_VALUE)
	public Response addConnectorType( @RequestBody ConnectorTypeIncomingDto connectorTypeIncomingDto)
	{
		logger.info("***ConnectorTypeController addConnectorType***");
		return Response.created().setPayload(connectorTypeService.addConnectorType(connectorTypeIncomingDto));
	}
	@PostMapping( value = "/editConnectorType" , consumes = APPLICATION_JSON_VALUE)
	public Response editConnectorType(@Valid @RequestBody ConnectorTypeIncomingDto connectorTypeIncomingDto)
	{
		logger.info("***ConnectorTypeController editConnectorType***");
		return Response.created().setPayload(connectorTypeService.editConnectorType(connectorTypeIncomingDto));
	}
     
	@GetMapping( value = "/deleteConnector/{id}" )
	public Response deleteConnectorType(@PathVariable("id") String id)
	{
		logger.info("***ConnectorTypeController deleteConnectorType***");
		
		return Response.ok().setPayload(connectorTypeService.deleteConnectorType(id));
	}
	
	@GetMapping( value = "/getConnectorById/{id}" )
	public Response getConnectorById(@PathVariable("id") String id) {
		logger.info("***ConnectorTypeController  getConnectorById ***");
	//	logger.info(NEW_ORDER_LOG, createdUser.toString());
		return Response.ok().setPayload(connectorTypeService.getConnectorTypeById(id));
		
	}
	@PostMapping(value = "/uploadConnectorType")
	public ResponseEntity<Response> uploadConnectorType (@RequestParam("file") MultipartFile file) {
		
		System.out.println("*******uploadCarModel********"+ file);
		Response res=null;
		res = new Response();
		String path = connectorTypeService.saveDocument(file);
		res.setRespData(path);
		return ResponseEntity.ok(res);

	
	}	


}