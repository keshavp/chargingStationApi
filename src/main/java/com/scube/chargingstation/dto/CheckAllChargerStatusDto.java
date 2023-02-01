package com.scube.chargingstation.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;


@Accessors(chain = true)
@NoArgsConstructor
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Setter
@Getter
public class CheckAllChargerStatusDto {
	
	private String issueReportedDateTime;
	
	private String chargingPointId;
	private String chargingPointName;
	private String chargingPointStatus;
	
	private String connectorId;
	private String connectorStatus;
	
	private String created_at_update;
	
	private String charging_point_id;
	private String charging_point_name;
	private String charging_point_status;
	
	private String connector_id;
	private String connector_status;
	
}
