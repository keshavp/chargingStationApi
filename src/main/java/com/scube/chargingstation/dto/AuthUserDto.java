package com.scube.chargingstation.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthUserDto {
	
	private String userid;
	private String username;
	private String empcode;
	private String firstnm;
	private String lastnm;
	private String role;
	private String accessToken;
	private String tokenType;
	private String refreshToken;



}