package com.scube.chargingstation.dto.incoming;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.scube.chargingstation.dto.AmenityDto;

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
public class ChargingPointIncomingDto {

	private String id;
	private String chargingPointId;
	private String name;
	private String partnerName;
	private String startTime;
	private String endTime;
	private String status;
	
	private String address;
	private String address2;
	private String pincode;
	private Double longitude;
	private Double latitude;

	private Double distance;
	private Double rating;
	
	private String manufractures;
	private String communicationtype;
	private String powerstandards;
	private String stationtype;
	
    private Set<ConnectorsIncomingDto> connectors;
    private Set<AmenityDto> amenities ;
}
