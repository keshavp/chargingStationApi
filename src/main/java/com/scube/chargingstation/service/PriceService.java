package com.scube.chargingstation.service;

import java.util.List;

import javax.validation.Valid;

import com.scube.chargingstation.dto.ChargingPointConnectorRateDto;
import com.scube.chargingstation.dto.PriceDto;
import com.scube.chargingstation.dto.incoming.ChargingPointConnectorRateIncomingDto;
import com.scube.chargingstation.entity.ChargingPointConnectorRateEntity;
import com.scube.chargingstation.entity.ChargingPointEntity;
import com.scube.chargingstation.entity.ConnectorEntity;

public interface PriceService {

	boolean addPrice(@Valid ChargingPointConnectorRateIncomingDto chargingPointConnectorRateIncomingDto);

	boolean addPriceRate(@Valid ChargingPointConnectorRateIncomingDto chargingPointConnectorRateIncomingDto);

	boolean editPriceRate(@Valid ChargingPointConnectorRateIncomingDto chargingPointConnectorRateIncomingDto);
	
	List<PriceDto> getAllPricingDetailsForAllStations();
	
//	ChargingPointConnectorRateEntity getPricingDetailsById(String id);
	
	PriceDto getPricingHistoryById(String id);

	List<PriceDto> getPricingByChargingPointAndConnector(String chargingPoint, String connector);

	boolean deletePrice(String cpid, String cid);
}
