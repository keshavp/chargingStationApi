package com.scube.chargingstation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scube.chargingstation.dto.ChargingPointConnectorRateDto;
import com.scube.chargingstation.dto.mapper.ChargingPointConnectorRateMapper;
import com.scube.chargingstation.entity.ChargingPointConnectorRateEntity;
import com.scube.chargingstation.entity.ChargingPointEntity;
import com.scube.chargingstation.entity.ConnectorEntity;
import com.scube.chargingstation.repository.ChargingPointConnectorRateRepository;

@Service
public class ChargingPointConnectorRateServiceImpl implements ChargingPointConnectorRateService {

	@Autowired
	ChargingPointConnectorRateRepository chargingPointConnectorRateRepository;
	
	@Autowired
	ConnectorService	connectorService;
	
	@Autowired
	ChargingPointService	chargingPointService;
	
	@Override
	public ChargingPointConnectorRateDto getConnectorByChargingPointIdAndConnectorIdAndAmount( String chargingPoint, String connector, String amount) {
		// TODO Auto-generated method stub
		
		ChargingPointEntity	chargingPointEntity = chargingPointService.getChargingPointEntityById(chargingPoint);
		
		ConnectorEntity	connectorEntity = connectorService.getConnectorEntityById(connector) ;
		
		ChargingPointConnectorRateEntity chargingPointConnectorRateEntity = chargingPointConnectorRateRepository.findByChargingPointEntityAndConnectorEntityAndAmount(chargingPointEntity,connectorEntity,amount);
		
		return ChargingPointConnectorRateMapper.toChargingPointConnectorRateDto(chargingPointConnectorRateEntity);
	}

	@Override
	public ChargingPointConnectorRateDto getConnectorByChargingPointNameAndConnectorNameAndAmount(String chargingPointName, String connectorName, String amount) {
		// TODO Auto-generated method stub
		
		ChargingPointEntity	chargingPointEntity = chargingPointService.getChargingPointEntityByChargingPointId(chargingPointName);
		
		ConnectorEntity	connectorEntity = connectorService.getConnectorEntityByConnectorIdAndChargingPointEntity(connectorName ,chargingPointEntity) ;
		
		ChargingPointConnectorRateEntity chargingPointConnectorRateEntity = chargingPointConnectorRateRepository.findByChargingPointEntityAndConnectorEntityAndAmount(chargingPointEntity,connectorEntity,amount);
		
		return ChargingPointConnectorRateMapper.toChargingPointConnectorRateDto(chargingPointConnectorRateEntity);
	}

	@Override
	public ChargingPointConnectorRateDto getConnectorByChargingPointNameAndConnectorIdAndAmount(String chargingPointName, String connectorId, String amount) {
		// TODO Auto-generated method stub
		ChargingPointEntity	chargingPointEntity = chargingPointService.getChargingPointEntityByChargingPointId(chargingPointName);
		
		ConnectorEntity	connectorEntity = connectorService.getConnectorEntityByIdAndChargingPointEntity(connectorId ,chargingPointEntity) ;
		
		ChargingPointConnectorRateEntity chargingPointConnectorRateEntity = chargingPointConnectorRateRepository.findByChargingPointEntityAndConnectorEntityAndAmount(chargingPointEntity,connectorEntity,amount);
		
		return ChargingPointConnectorRateMapper.toChargingPointConnectorRateDto(chargingPointConnectorRateEntity);
	}
}
