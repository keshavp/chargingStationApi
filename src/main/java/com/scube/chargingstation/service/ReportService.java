package com.scube.chargingstation.service;

import java.util.List;

import com.scube.chargingstation.dto.ChargingRequestRespDto;
import com.scube.chargingstation.dto.incoming.ChargingStationWiseReportIncomingDto;

public interface ReportService {

	
	public List<ChargingRequestRespDto> getChargingDetailsForAllStations(ChargingStationWiseReportIncomingDto chargingStationWiseReportIncomingDto);

	
}
