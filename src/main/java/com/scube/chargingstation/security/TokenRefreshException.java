package com.scube.chargingstation.security;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class TokenRefreshException extends RuntimeException {

	  private static final long serialVersionUID = 1L;

	  public TokenRefreshException(String token, String message) {
	    super(String.format("Failed for [%s]: %s", token, message));
	  }
	}