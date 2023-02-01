package com.scube.chargingstation.dto.incoming;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ServerAPIResetIncomingDto {
	
	private String startDate;
	private String endDate;
	
	private String chargingPointId;
	private String connectorId;

}
