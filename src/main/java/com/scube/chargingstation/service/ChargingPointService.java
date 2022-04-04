package com.scube.chargingstation.service;

import com.scube.chargingstation.dto.ChargingPointDto;
import com.scube.chargingstation.entity.ChargingPointEntity;

public interface ChargingPointService {

	ChargingPointDto getChargingPointById(String id);
	
	ChargingPointEntity	getChargingPointEntityById(String id);

	ChargingPointEntity getChargingPointEntityByChargingPointId(String connectorName);
}
