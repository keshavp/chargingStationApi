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

import com.scube.chargingstation.dto.ComplaintResponseCommentsDto;
import com.scube.chargingstation.dto.incoming.ComplaintIncomingDto;
import com.scube.chargingstation.dto.response.Response;
import com.scube.chargingstation.service.ComplaintService;


@CrossOrigin (origins = "*", maxAge = 3600)
@RestController
@RequestMapping (path = {"api/v1/comlaintManagement"}, produces = APPLICATION_JSON_VALUE)
public class ComplaintManagementController {
	
	@Autowired
	ComplaintService complaintService;
	
	private static final Logger logger = LoggerFactory.getLogger(ComplaintManagementController.class);
	
	
	// Make a Complaint API - For User
	@PostMapping (value = "/makeNewComplaint", consumes = APPLICATION_JSON_VALUE)
	public Response addNewComplaint(@Valid @RequestBody ComplaintIncomingDto complaintIncomingDto) {
		
		logger.info("***ComplaintManagementController makeNewComplaint***");		
		return Response.created().setPayload(complaintService.addNewComplaint(complaintIncomingDto));
		
	}
	
	// User Complaint Info - For User
	@GetMapping (value = "/getUserComplaintDetailsByUserId/{userMobileNo}")
	public Response getComplaintDetailsByUserId(@PathVariable ("userMobileNo") String userMobileNo) {
		
		logger.info("***ComplaintManagementController getUserComplaintDetailsByUserId***");
		return Response.ok().setPayload(complaintService.getComplaintDetailsByUserId(userMobileNo));
	}
	
	
	// Add Comment for User Complaint - For Admin
	@PostMapping (value = "/addCommmentForUserComplaint") 
	public Response addCommmentForUserComplaint(@Valid @RequestBody ComplaintResponseCommentsDto complaintResponseCommentsDto) {
		
		logger.info("***ComplaintManagementController addCommmentForUserComplaint***");
		return Response.ok().setPayload(complaintService.addCommmentForUserComplaint(complaintResponseCommentsDto));
	}
	
	// Get User Complaint Info - For Admin
	@GetMapping (value = "/getAllComplaintInfo")
	public Response getAllComplaintList() {
		
		logger.info("***ComplaintManagementController getAllComplaintList***");	
		return Response.ok().setPayload(complaintService.getAllComplaintInfoList());
		
	}
	
	// Get User Complaint Info By Complaint ID - For Admin
	@GetMapping (value = "/getComplaintInfoById/{id}")
	public Response getComplaintInfoById(@PathVariable ("id") String id) {
		
		logger.info("***ComplaintManagementController getComplaintInfoById***");	
		return Response.ok().setPayload(complaintService.getComplaintInfoById(id));
	}
	
	// Close Complaint - For Admin
	@GetMapping (value = "/closeComplaintByComplaintId/{id}/{userid}") 
	public Response closeComplaintForUser(@PathVariable ("id") String id, @PathVariable ("userid") String userid) {
		
		logger.info("***ComplaintManagementController closeComplaintForUser***");
		return Response.ok().setPayload(complaintService.closeComplaint(id,userid));
		
	}
	
	
}
