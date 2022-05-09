package com.scube.chargingstation.service;

import java.util.List;

import javax.validation.Valid;

import com.scube.chargingstation.dto.ConnectorDto;
import com.scube.chargingstation.dto.incoming.ConnectorTypeIncomingDto;
import com.scube.chargingstation.entity.ChargingPointEntity;
import com.scube.chargingstation.entity.ConnectorEntity;

public interface ConnectorService {

	ConnectorDto getConnectorById(String id);
	
	ConnectorEntity getConnectorEntityById(String id);

	ConnectorEntity getConnectorEntityByConnectorIdAndChargingPointEntity(String connectorId,
			ChargingPointEntity chargingPointEntity);

	ConnectorEntity getConnectorEntityByIdAndChargingPointEntity(String id,
			ChargingPointEntity chargingPointEntity);

	}
