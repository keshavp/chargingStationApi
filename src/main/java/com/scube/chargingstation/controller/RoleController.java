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

import com.scube.chargingstation.dto.incoming.RoleIncomingDto;
import com.scube.chargingstation.dto.response.Response;
import com.scube.chargingstation.service.RoleService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = {"/api/v1/role"}, produces = APPLICATION_JSON_VALUE)
public class RoleController {

	@Autowired
	RoleService  roleService;
	
	private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

//	@Operation(summary = "add user info")
//	@ApiResponse(responseCode = "200", description = "add user info", content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserInfoIncomingDto.class))})
	@PostMapping( value = "/addRole" , consumes = APPLICATION_JSON_VALUE)
	public Response addRole(@Valid @RequestBody RoleIncomingDto roleIncomingDto) {
		logger.info("***RoleController addRole***");
	//	logger.info(NEW_ORDER_LOG, createdUser.toString());
		return Response.created().setPayload(roleService.addRole(roleIncomingDto));
		
	}
	@PostMapping( value = "/editRole" , consumes = APPLICATION_JSON_VALUE)
	public Response editRole(@Valid @RequestBody RoleIncomingDto roleIncomingDto) {
		logger.info("***RoleController editRole***");
	//	logger.info(NEW_ORDER_LOG, createdUser.toString());
	
		return Response.ok().setPayload(roleService.editRole(roleIncomingDto));
		
	}
	@GetMapping( value = "/deleteRole/{id}" )
	public Response deleteRole(@PathVariable("id") String id) {
		logger.info("***RoleController deleteRole***");
	//	logger.info(NEW_ORDER_LOG, createdUser.toString());
		return Response.ok().setPayload(roleService.deleteRole(id));
		
	}
	@GetMapping( value = "/roles" )
	public Response findAllRoles() {
		logger.info("***RoleController findALLRoles***");
	//	logger.info(NEW_ORDER_LOG, createdUser.toString());
		return Response.ok().setPayload(roleService.findAllRoles());
		
	}
	@GetMapping( value = "/activeRoles" )
	public Response findActiveRoles() {
		logger.info("***RoleController findActiveRoles***");
	//	logger.info(NEW_ORDER_LOG, createdUser.toString());
		return Response.ok().setPayload(roleService.findActiveRoles());
		
	}
	@GetMapping( value = "/getRoleById/{id}" )
	public Response getRoleById(@PathVariable("id") String id) {
		logger.info("***RoleController RoleById***");
	//	logger.info(NEW_ORDER_LOG, createdUser.toString());
		return Response.ok().setPayload(roleService.getRoleById(id));
		
	}
	
}
