package com.scube.chargingstation.service;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scube.chargingstation.dto.ChargingPointConnectorDto;
import com.scube.chargingstation.dto.ChargingPointConnectorRateDto;
import com.scube.chargingstation.dto.ChargingPointDto;
import com.scube.chargingstation.entity.ChargingPointConnectorRateEntity;
import com.scube.chargingstation.entity.ChargingPointEntity;
import com.scube.chargingstation.entity.ConnectorEntity;
import com.scube.chargingstation.repository.ChargingPointConnectorRateRepository;

@Service
public class PriceServiceImpl implements PriceService{

	@Autowired
	ChargingPointConnectorRateRepository chargingPointConnectorRateRepository;
	ChargingPointConnectorRateService chargingPointConnectorRateService;
	@Autowired
	ConnectorService	connectorService;
	
	@Autowired
	ChargingPointService	chargingPointService;

	
	@Override
	public boolean addPrice(@Valid ChargingPointConnectorRateDto chargingPointConnectorRateDto) {
		// TODO Auto-generated method stub

		
		
		
		
		ChargingPointConnectorRateEntity chargingpointconnectorRateEntity=new ChargingPointConnectorRateEntity();
		
	//	chargingpointconnectorRateEntity.setChargingPointEntity(chargingPointConnectorRateDto.getConnectorCode());
	//	chargingpointconnectorRateEntity.setConnectorEntity(chargingPointConnectorRateDto.getChargingPointCode());
		chargingpointconnectorRateEntity.setAmount(chargingPointConnectorRateDto.getAmount());
		chargingpointconnectorRateEntity.setKwh(chargingPointConnectorRateDto.getKWh());
		chargingpointconnectorRateEntity.setIsdeleted("N");
		chargingPointConnectorRateRepository.save(chargingpointconnectorRateEntity);
		
		return true;
	}
	
}
