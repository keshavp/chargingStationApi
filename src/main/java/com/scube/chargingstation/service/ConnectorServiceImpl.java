  package com.scube.chargingstation.service;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scube.chargingstation.dto.ChargingPointConnectorDto;
import com.scube.chargingstation.dto.incoming.ConnectorTypeIncomingDto;
import com.scube.chargingstation.dto.mapper.ConnectorMapper;
import com.scube.chargingstation.entity.ChargerTypeEntity;
import com.scube.chargingstation.entity.ChargingPointEntity;
import com.scube.chargingstation.entity.ConnectorEntity;
import com.scube.chargingstation.exception.BRSException;
import com.scube.chargingstation.repository.ConnectorRepository;

@Service
public class ConnectorServiceImpl implements ConnectorService {

	@Autowired
	ConnectorRepository connectorRepository;
	private static final Logger logger = LoggerFactory.getLogger(CarModelServiceImpl.class);
	
	@Override
	public ChargingPointConnectorDto getConnectorById(String id) {
		// TODO Auto-generated method stub
		
		ConnectorEntity connectorEntity = connectorRepository.findById(id).get();
		
		ChargingPointConnectorDto connectorDto = ConnectorMapper.toConnectorDto(connectorEntity);
		
		return connectorDto;
	}

	@Override
	public ConnectorEntity getConnectorEntityById(String id) {
		// TODO Auto-generated method stub
		return connectorRepository.findById(id).get();
	}

	@Override
	public ConnectorEntity getConnectorEntityByConnectorIdAndChargingPointEntity(String chargingPointName,
			ChargingPointEntity chargingPointEntity) {
		// TODO Auto-generated method stub
		return connectorRepository.findByConnectorIdAndChargingPointEntity(chargingPointName,chargingPointEntity);
	}

	@Override
	public ConnectorEntity getConnectorEntityByIdAndChargingPointEntity(String id,
			ChargingPointEntity chargingPointEntity) {
		// TODO Auto-generated method stub
		return connectorRepository.findByIdAndChargingPointEntity(id,chargingPointEntity);
	}

	@Override
	public ConnectorEntity getConnectorEntityByConnectorId(String chargerId) {
		// TODO Auto-generated method stub
		return connectorRepository.findByConnectorId(chargerId);
	}


}
