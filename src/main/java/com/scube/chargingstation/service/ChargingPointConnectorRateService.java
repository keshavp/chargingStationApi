package com.scube.chargingstation.service;

import com.scube.chargingstation.dto.ChargingPointConnectorRateDto;

public interface ChargingPointConnectorRateService {

	ChargingPointConnectorRateDto getConnectorByChargingPointIdAndConnectorIdAndAmount( String chargingPoint,String connector , String amount);
	
	ChargingPointConnectorRateDto getConnectorByChargingPointNameAndConnectorNameAndAmount( String chargingPointName, String connectorName , String amount);
	
	ChargingPointConnectorRateDto getConnectorByChargingPointNameAndConnectorIdAndAmount( String chargingPointName, int connectorId , String amount);
}
