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
public class CcavenueInitDto {

	
	String OrderId;
	String AccessCode;
	String RedirectUrl;
	String CancelUrl;
	String EncVal;
	
	/*
	 * "order_id": 3881885, "access_code": "AVHQ89GL40BJ77QHJB", "redirect_url":
	 * "www.amazonaws.com/payment/redirect.php", "cancel_url":
	 * "www.amazonaws.com/payment/cancel.php", "enc_val":
	 */
}
