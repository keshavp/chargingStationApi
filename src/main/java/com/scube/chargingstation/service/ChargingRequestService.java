package com.scube.chargingstation.service;

import java.util.List;

import com.scube.chargingstation.dto.incoming.ChargingRequestDto;
import com.scube.chargingstation.entity.ChargingPointEntity;
import com.scube.chargingstation.entity.ChargingRequestEntity;
import com.scube.chargingstation.entity.ConnectorEntity;


public interface ChargingRequestService {

	
	public boolean addChargingRequest(ChargingRequestDto chargingRequestDto) ;
	
	List<ChargingRequestEntity> findChargingRequestEntityByChargingStatus(String chargingStatus);

	public ChargingRequestEntity findChargingRequestEntityByChargingPointEntityAndConnectorEntityAndStatus(
			ChargingPointEntity chargingPointEntity, ConnectorEntity connectorEntity, String string);
}
