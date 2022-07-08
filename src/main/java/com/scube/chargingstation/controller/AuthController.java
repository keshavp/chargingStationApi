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

import com.scube.chargingstation.dto.AuthUserDto;
import com.scube.chargingstation.dto.incoming.ForgetPasswordIncomingDto;
import com.scube.chargingstation.dto.incoming.SetNewPasswordIncomingDto;
import com.scube.chargingstation.dto.incoming.UserLoginIncomingDto;
import com.scube.chargingstation.dto.response.Response;
import com.scube.chargingstation.service.AuthService;

/*import io.swagger.v3.oas.annotations.Operation;
  import io.swagger.v3.oas.annotations.media.Content;
  import io.swagger.v3.oas.annotations.media.Schema;
  import io.swagger.v3.oas.annotations.responses.ApiResponse;
  import io.swagger.v3.oas.annotations.tags.Tag;
*/
  @CrossOrigin(origins = "*", maxAge = 3600)
  @RestController
  @RequestMapping(path = {"/api/v1/auth"}, produces = APPLICATION_JSON_VALUE)
  //@Tag(name = "UserLogin", description = "the user Login auth API with documentation annotations")
  public class AuthController {

  	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

  	@Autowired
  	private AuthService authService;
  	
  	@SuppressWarnings("rawtypes")
 // 	@Operation(summary = "User Login")
 // 	@ApiResponse(responseCode = "201", description = "user login successfully", content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = AuthUserDto.class))})
  	@PostMapping( value = "/signin" , consumes = APPLICATION_JSON_VALUE)
  	public Response userSignIn(@Valid @RequestBody UserLoginIncomingDto loginRequest) {
  		
  		 AuthUserDto responseData;
  		 
  		 responseData = authService.authenticateUser(loginRequest);
  		 
  		 logger.info("Authentication successful!");
  		 
  		 return Response.ok().setPayload(responseData);
  	}
  	
  	
  //	@Operation(summary = "Reset Password")
  //	@ApiResponse(responseCode = "200", description = "Reset Password", content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = SetNewPasswordIncomingDto.class))})
  	@PostMapping( value = "/setNewPassword" , consumes = APPLICATION_JSON_VALUE)
  	public Response resetPassword(@Valid @RequestBody SetNewPasswordIncomingDto setNewPasswordIncomingDto) {
  		logger.info("***AuthController setNewPassword***");
  	//	logger.info(NEW_ORDER_LOG, createdUser.toString());
  		return Response.ok().setPayload(authService.setNewPassword(setNewPasswordIncomingDto));
  		
  	}
  	
 // 	@Operation(summary = "check email to reset pass")
 // 	@ApiResponse(responseCode = "200", description = "check email to reset pass", content = {@Content(mediaType = APPLICATION_JSON_VALUE)})
  	@GetMapping( value = "/checkResetPassword/{email}" , consumes = APPLICATION_JSON_VALUE)
  	public Response checkEmail(@Valid @RequestBody @PathVariable String email) {
  		logger.info("***AuthController checkResetPassword***");
  	//	logger.info(NEW_ORDER_LOG, createdUser.toString());
  		return Response.ok().setPayload(authService.checkResetPasswordConditions(email));
  		
  	}
  	
  //	@Operation(summary = "Reset Password")
 // 	@ApiResponse(responseCode = "200", description = "Reset Password", content = {@Content(mediaType = APPLICATION_JSON_VALUE)})
  	@PostMapping( value = "/resetPassword/{email}" , consumes = APPLICATION_JSON_VALUE)
  	public Response resetPassword(@Valid @RequestBody @PathVariable String email) {
  		logger.info("***AuthController resetPassword***");
  	//	logger.info(NEW_ORDER_LOG, createdUser.toString());
  		return Response.ok().setPayload(authService.resetPassword(email));
  		
  	}
  	
  	
  	@PostMapping( value = "/signout" , consumes = APPLICATION_JSON_VALUE)
  	public Response userSignOut(@Valid @RequestBody UserLoginIncomingDto loginRequest) {
  		
  		 
  		 logger.info("signout successful!");
  		 
  		 return Response.ok().setPayload(authService.signoutUser(loginRequest));
  	}

	
	@PostMapping( value = "/generateNewOtp", consumes = APPLICATION_JSON_VALUE) 
	public Response generateNewOtp(@Valid @RequestBody ForgetPasswordIncomingDto forgetPasswordIncomingDto) {
		  
		  logger.info("***AuthController generateNewOtp***");
		  return Response.ok().setPayload(authService.generateNewOtp(forgetPasswordIncomingDto)); 
	}
	
	
	@PostMapping( value = "/validateGeneratedOtp", consumes = APPLICATION_JSON_VALUE)
	public Response validateGeneratedOtp(@Valid @RequestBody ForgetPasswordIncomingDto forgetPasswordIncomingDto) {
		
		logger.info("***AuthController validateGeneratedOtp***");
		
		return Response.ok().setPayload(authService.validateGeneratedOtp(forgetPasswordIncomingDto));
	}

	
	@PostMapping( value = "/deleteUserAccount" , consumes = APPLICATION_JSON_VALUE)
  	public Response deleteUserAccount(@Valid @RequestBody SetNewPasswordIncomingDto setNewPasswordIncomingDto) {
  		logger.info("***AuthController deleteUserAccount***");
  	//	logger.info(NEW_ORDER_LOG, createdUser.toString());
  		return Response.ok().setPayload(authService.deleteUserAccount(setNewPasswordIncomingDto));
  		
  	}
	
}
