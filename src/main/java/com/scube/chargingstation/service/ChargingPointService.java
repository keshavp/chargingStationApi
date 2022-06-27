package com.scube.chargingstation.service;

import java.util.List;

import javax.validation.Valid;

import com.scube.chargingstation.dto.ChargingPointDto;
import com.scube.chargingstation.dto.incoming.ChargingPointIncomingDto;
import com.scube.chargingstation.entity.ChargingPointEntity;
import com.scube.chargingstation.entity.PartnerInfoEntity;

public interface ChargingPointService {

	ChargingPointDto getChargingPointById(String id);
	
	ChargingPointEntity	getChargingPointEntityById(String id);

	ChargingPointEntity getChargingPointEntityByChargingPointId(String connectorName);

	Boolean addChargingPoint(@Valid ChargingPointIncomingDto chargingPointIncomingDto);

	List<ChargingPointDto> getAllChargingStations();
	
	List<ChargingPointEntity> getAllChargingPointEntity();

	Boolean updateChargingPoint(@Valid ChargingPointIncomingDto chargingPointIncomingDto);

	List<ChargingPointDto> getAllActiveChargingStations();
	
	ChargingPointDto getChargingStationsById(String id);
	
	Boolean getChargingStationsgoLiveCheckById(String id);
	
	PartnerInfoEntity findByPartnerId(String id);

	List<ChargingPointDto> getAllChargingStationsByUserMobile(String id);
	
	List<ChargingPointDto> getNearByChargingPoints(Double lat,Double longi);

}
