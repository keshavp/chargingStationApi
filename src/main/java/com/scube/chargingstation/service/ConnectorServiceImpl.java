package com.scube.chargingstation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scube.chargingstation.dto.ConnectorDto;
import com.scube.chargingstation.dto.mapper.ConnectorMapper;
import com.scube.chargingstation.entity.ChargingPointEntity;
import com.scube.chargingstation.entity.ConnectorEntity;
import com.scube.chargingstation.repository.ConnectorRepository;

@Service
public class ConnectorServiceImpl implements ConnectorService {

	@Autowired
	ConnectorRepository connectorRepository;
	
	@Override
	public ConnectorDto getConnectorById(String id) {
		// TODO Auto-generated method stub
		
		ConnectorEntity connectorEntity = connectorRepository.findById(id).get();
		
		ConnectorDto connectorDto = ConnectorMapper.toConnectorDto(connectorEntity);
		
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

}
