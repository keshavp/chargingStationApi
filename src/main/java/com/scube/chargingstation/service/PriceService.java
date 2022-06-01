package com.scube.chargingstation.service;

import javax.validation.Valid;

import com.scube.chargingstation.dto.ChargingPointConnectorRateDto;

public interface PriceService {

	boolean addPrice(@Valid ChargingPointConnectorRateDto chargingPointConnectorRateDto);
	
}
