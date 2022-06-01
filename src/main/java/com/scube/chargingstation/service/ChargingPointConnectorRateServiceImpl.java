package com.scube.chargingstation.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	
	private static final Logger logger = LoggerFactory.getLogger(ChargingPointConnectorRateServiceImpl.class);

	
	@Override
	public ChargingPointConnectorRateDto getConnectorByChargingPointIdAndConnectorIdAndAmount( String chargingPoint, String connector, double amount) {
		// TODO Auto-generated method stub
		
		ChargingPointEntity	chargingPointEntity = chargingPointService.getChargingPointEntityById(chargingPoint);
		
		ConnectorEntity	connectorEntity = connectorService.getConnectorEntityById(connector) ;
		
		ChargingPointConnectorRateEntity chargingPointConnectorRateEntity = chargingPointConnectorRateRepository.findByChargingPointEntityAndConnectorEntityAndAmount(chargingPointEntity,connectorEntity,amount);
		
		return ChargingPointConnectorRateMapper.toChargingPointConnectorRateDto(chargingPointConnectorRateEntity);
	}

	@Override
	public ChargingPointConnectorRateDto getConnectorByChargingPointNameAndConnectorNameAndAmount(String chargingPointName, String connectorName, double amount) {
		// TODO Auto-generated method stub
		
		ChargingPointEntity	chargingPointEntity = chargingPointService.getChargingPointEntityByChargingPointId(chargingPointName);
		
		ConnectorEntity	connectorEntity = connectorService.getConnectorEntityByConnectorIdAndChargingPointEntity(connectorName ,chargingPointEntity) ;
		
		ChargingPointConnectorRateEntity chargingPointConnectorRateEntity = chargingPointConnectorRateRepository.findByChargingPointEntityAndConnectorEntityAndAmount(chargingPointEntity,connectorEntity,amount);
		
		return ChargingPointConnectorRateMapper.toChargingPointConnectorRateDto(chargingPointConnectorRateEntity);
	}
   
	@Override
	public ChargingPointConnectorRateDto getConnectorByChargingPointNameAndConnectorIdAndAmount(String chargingPointName, String connectorId, double amount) {
		// TODO Auto-generated method stub
		ChargingPointEntity	chargingPointEntity = chargingPointService.getChargingPointEntityByChargingPointId(chargingPointName);
		
		logger.info("cp id"+chargingPointEntity.getId());    
		
	//	ConnectorEntity	connectorEntity = connectorService.getConnectorEntityByIdAndChargingPointEntity(connectorId ,chargingPointEntity) ;
		
		ConnectorEntity	connectorEntity = connectorService.getConnectorEntityByConnectorIdAndChargingPointEntity(connectorId ,chargingPointEntity) ;
		
		
		logger.info("eonnector id ---"+connectorEntity.getId()); 
		
		ChargingPointConnectorRateEntity chargingPointConnectorRateEntity = chargingPointConnectorRateRepository.findByChargingPointEntityAndConnectorEntityAndAmount(chargingPointEntity,connectorEntity,amount);
		
		logger.info("rate id ----"+chargingPointConnectorRateEntity.getId());
		
		logger.info("rate amount----"+chargingPointConnectorRateEntity.getAmount());
		
		return ChargingPointConnectorRateMapper.toChargingPointConnectorRateDto(chargingPointConnectorRateEntity);
	}
	
	@Override
	public ChargingPointConnectorRateDto getConnectorByChargingPointNameAndConnectorIdAndKwh(String chargingPointName, String connectorId, double kwh) {
		// TODO Auto-generated method stub
		ChargingPointEntity	chargingPointEntity = chargingPointService.getChargingPointEntityByChargingPointId(chargingPointName);
		
		ConnectorEntity	connectorEntity = connectorService.getConnectorEntityByIdAndChargingPointEntity(connectorId ,chargingPointEntity) ;
		
		ChargingPointConnectorRateEntity chargingPointConnectorRateEntity = chargingPointConnectorRateRepository.findByChargingPointEntityAndConnectorEntityAndKwh(chargingPointEntity,connectorEntity,kwh);
		
		return ChargingPointConnectorRateMapper.toChargingPointConnectorRateDto(chargingPointConnectorRateEntity);
	}
}
