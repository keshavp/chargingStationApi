package com.scube.chargingstation.controller;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scube.chargingstation.dto.incoming.OtpVerificationIncomingDto;
import com.scube.chargingstation.dto.incoming.UserInfoIncomingDto;
import com.scube.chargingstation.dto.incoming.UserWalletRequestDto;
import com.scube.chargingstation.dto.response.Response;
import com.scube.chargingstation.service.UserInfoService;

/*import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
*/
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = {"/api/v1/user"}, produces = APPLICATION_JSON_VALUE)
public class UserInfoController {
	
	@Autowired
	UserInfoService userInfoService;
	
	private static final Logger logger = LoggerFactory.getLogger(UserInfoController.class);
	
//	@Operation(summary = "add user info")
//	@ApiResponse(responseCode = "200", description = "add user info", content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserInfoIncomingDto.class))})
	@PostMapping( value = "/addUser" , consumes = APPLICATION_JSON_VALUE)
	public Response addUser(@Valid @RequestBody UserInfoIncomingDto userInfoIncomingDto) {
		logger.info("***UserInfoController addUser***");
	//	logger.info(NEW_ORDER_LOG, createdUser.toString());
		return Response.ok().setPayload(userInfoService.addUserInfo(userInfoIncomingDto));
		
	}
	
	
	@PostMapping( value = "/mobileOtpVerification" , consumes = APPLICATION_JSON_VALUE)
	public Response mobileOtpVerification(@Valid @RequestBody OtpVerificationIncomingDto otpVerificationIncomingDto) {
		logger.info("***UserInfoController addUser***");
	//	logger.info(NEW_ORDER_LOG, createdUser.toString());
		return Response.ok().setPayload(userInfoService.moblieOtpVerify(otpVerificationIncomingDto));
		
	}
	
	//edit profile for mobile app user
	@PostMapping( value = "/editUserProfile" , consumes = APPLICATION_JSON_VALUE)
	public Response editUserProfile(@Valid @RequestBody UserInfoIncomingDto userInfoIncomingDto) {
		logger.info("***editUserProfile***");
		
				return Response.ok().setPayload(userInfoService.editUserProfile(userInfoIncomingDto));
		
	}
	
	
	
}
