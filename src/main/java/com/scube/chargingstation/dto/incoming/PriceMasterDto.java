package com.scube.chargingstation.dto.incoming;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.scube.chargingstation.dto.ChargerTypeDto;

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

public class PriceMasterDto {
	
	private double amount;
	private double chargingAmount;
	private double cgst;
	private double sgst;
	private double kwh;
	private String time;
	private String id;

}
