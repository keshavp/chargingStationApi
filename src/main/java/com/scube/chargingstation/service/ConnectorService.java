package com.scube.chargingstation.service;

import java.util.List;
import com.scube.chargingstation.dto.ChargingPointConnectorDto;
import com.scube.chargingstation.entity.ChargingPointEntity;
import com.scube.chargingstation.entity.ConnectorEntity;

public interface ConnectorService {

	ChargingPointConnectorDto getConnectorById(String id);
	
	ConnectorEntity getConnectorEntityById(String id);

	ConnectorEntity getConnectorEntityByConnectorIdAndChargingPointEntity(String connectorId,
			ChargingPointEntity chargingPointEntity);

	ConnectorEntity getConnectorEntityByIdAndChargingPointEntity(String id,
			ChargingPointEntity chargingPointEntity);

	ConnectorEntity getConnectorEntityByConnectorId(String chargerId);

	boolean deleteConnector(String id);	
	
	List<ConnectorEntity> getConnectorEntityByChargingPoint(String chargingPoint);

	List<ConnectorEntity> getAllConnectorEntityListByChargingPointEntity(ChargingPointEntity chargingPoint);
	
}
