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
public class ChargingPointDto {

	private String address;
	private Double longitude;
	private Double distance;
	private Double latitude;
	private String name;
	private Double rating;
	private String status;
	private String chargingPointId;
	
    private Set<ConnectorDto> Connectors;
    private Set<AmenityDto> Amenities ;
}
