package com.scube.chargingstation.dto;
import java.util.Set;

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
public class UserCarRespDto {

	private String id;
	private String modelId;
	private String model;
	private String vehicleNo;
	//private String imagePath;
	//RUCER private Set<ChargerTypeDto> chargertypes ;
}
