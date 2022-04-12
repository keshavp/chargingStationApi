package com.scube.chargingstation.service;

import com.scube.chargingstation.dto.ChargingPointConnectorRateDto;

public interface ChargingPointConnectorRateService {

	ChargingPointConnectorRateDto getConnectorByChargingPointIdAndConnectorIdAndAmount( String chargingPoint,String connector , double amount);
	
	ChargingPointConnectorRateDto getConnectorByChargingPointNameAndConnectorNameAndAmount( String chargingPointName, String connectorName , double amount);
	
	ChargingPointConnectorRateDto getConnectorByChargingPointNameAndConnectorIdAndAmount( String chargingPointName, String connectorId , double amount);
	
	ChargingPointConnectorRateDto getConnectorByChargingPointNameAndConnectorIdAndKwh( String chargingPointName, String connectorId , double kwh);
}
