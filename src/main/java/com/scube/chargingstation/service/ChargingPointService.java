package com.scube.chargingstation.service;

import java.util.List;

import javax.validation.Valid;

import com.scube.chargingstation.dto.ChargingPointDto;
import com.scube.chargingstation.dto.incoming.ChargingPointIncomingDto;
import com.scube.chargingstation.entity.ChargingPointEntity;

public interface ChargingPointService {

	ChargingPointDto getChargingPointById(String id);
	
	ChargingPointEntity	getChargingPointEntityById(String id);

	ChargingPointEntity getChargingPointEntityByChargingPointId(String connectorName);

	Boolean addChargingPoint(@Valid ChargingPointIncomingDto chargingPointIncomingDto);

	List<ChargingPointDto> getAllChargingStations();

	Boolean updateChargingPoint(@Valid ChargingPointIncomingDto chargingPointIncomingDto);

	List<ChargingPointDto> getAllActiveChargingStations();
	
	ChargingPointDto getChargingStationsById(String id);
}
