package com.scube.chargingstation.service;

import javax.validation.Valid;

import com.scube.chargingstation.dto.ChargingPointConnectorRateDto;
import com.scube.chargingstation.dto.incoming.ChargingPointConnectorRateIncomingDto;
import com.scube.chargingstation.entity.ChargingPointEntity;
import com.scube.chargingstation.entity.ConnectorEntity;

public interface PriceService {

	boolean addPrice(@Valid ChargingPointConnectorRateIncomingDto chargingPointConnectorRateIncomingDto);

	boolean addPriceRate(@Valid ChargingPointConnectorRateIncomingDto chargingPointConnectorRateIncomingDto);

	boolean editPriceRate(@Valid ChargingPointConnectorRateIncomingDto chargingPointConnectorRateIncomingDto);
	
}
