package com.scube.chargingstation.service;

import java.util.List;

import javax.validation.Valid;

import com.scube.chargingstation.dto.ChargerTypeDto;
import com.scube.chargingstation.dto.ConnectorDto;
import com.scube.chargingstation.dto.ConnectorTypeDto;
import com.scube.chargingstation.dto.incoming.ConnectorTypeIncomingDto;
import com.scube.chargingstation.entity.ChargerTypeEntity;

public interface ConnectorTypeService {
	
	  List<ConnectorTypeDto> getConnectors();

		boolean addConnectorType( ConnectorTypeIncomingDto connectorTypeIncomingDto);

		boolean editConnectorType(@Valid ConnectorTypeIncomingDto connectorTypeIncomingDto);

		boolean deleteConnectorType(String id);

	
		ConnectorTypeDto getConnectorTypeById(String id);
}
