package com.scube.chargingstation.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import com.google.api.client.util.Value;
import com.google.common.net.MediaType;
import com.scube.chargingstation.dto.incoming.CarModelIncomingDto;
import com.scube.chargingstation.dto.response.Response;
import com.scube.chargingstation.service.CarModelService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = {"/api/v1/carmodel"}, produces = APPLICATION_JSON_VALUE)
public class CarModelController {
	
	
	@Autowired 
	CarModelService carModelService;
	private static final Logger logger = LoggerFactory.getLogger(CarModelController.class);
	
	@GetMapping(value ="/getCarModels")
	public Response getCarModels() throws Exception {
		
		return Response.ok().setPayload(carModelService.getCarModels());
	}
	@PostMapping( value = "/addCarModel" , consumes = APPLICATION_JSON_VALUE)
	public Response addCarModel(@Valid @RequestBody CarModelIncomingDto carModelIncomigDto)
	{
		logger.info("***CarModelController addCarModel***");
		return Response.created().setPayload(carModelService.addCarModel(carModelIncomigDto));
	}
	
	@PostMapping( value = "/editCarModel" , consumes = APPLICATION_JSON_VALUE)
	public Response editCarModel(@Valid @RequestBody CarModelIncomingDto carModelIncomigDto)
	{
		logger.info("***CarModelController editCarModel***");
		return Response.created().setPayload(carModelService.editCarModel(carModelIncomigDto));
	}
	
	@GetMapping( value = "/deleteCarModel/{id}" )
	public Response deleteCarModel(@PathVariable("id") String id) {
		logger.info("***CarModelController deleteCarModel***");
	//	logger.info(NEW_ORDER_LOG, createdUser.toString());
		return Response.ok().setPayload(carModelService.deleteCarModel(id));
	
   }
	@GetMapping( value = "/getCarModelById/{id}" )
	public Response getCarModelById(@PathVariable("id") String id) {
		logger.info("***CarModelController CARById***");
	//	logger.info(NEW_ORDER_LOG, createdUser.toString());
		return Response.ok().setPayload(carModelService.getRoleById(id));
		
	}
	
	/*@PostMapping(value = "/uploadCarModel",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)  // , 
	public ResponseEntity<Response> uploadPartnerLogo (@RequestParam("file") MultipartFile file) {
		
		System.out.println("*******uploadCarModel********"+ file);
		Response res=null;
		res = new Response();
		String path = carModelService.saveDocument(file);
		res..setRespData(path);
		return ResponseEntity.ok(res);

	}*/
	
}