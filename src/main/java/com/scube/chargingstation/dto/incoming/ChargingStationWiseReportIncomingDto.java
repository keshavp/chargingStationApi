package com.scube.chargingstation.dto.incoming;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ChargingStationWiseReportIncomingDto{

	private String chargePointId;
	private String startDate;
	private String endDate;
	private String partnerName;
}  