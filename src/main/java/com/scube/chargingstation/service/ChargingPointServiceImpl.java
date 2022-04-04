package com.scube.chargingstation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scube.chargingstation.dto.ChargingPointDto;
import com.scube.chargingstation.dto.ConnectorDto;
import com.scube.chargingstation.dto.mapper.ChargingPointMapper;
import com.scube.chargingstation.dto.mapper.ConnectorMapper;
import com.scube.chargingstation.entity.ChargingPointEntity;
import com.scube.chargingstation.entity.ConnectorEntity;
import com.scube.chargingstation.repository.ChargingPointRepository;

@Service
public class ChargingPointServiceImpl implements ChargingPointService {

	@Autowired
	ChargingPointRepository chargingPointRepository;
	
	@Override
	public ChargingPointDto getChargingPointById(String id) {
		// TODO Auto-generated method stub
		
		ChargingPointEntity chargingPointEntity = chargingPointRepository.findById(id).get();
		
		ChargingPointDto chargingPointDto = ChargingPointMapper.toChargingPointDto(chargingPointEntity);
		
		return chargingPointDto;
	}

	@Override
	public ChargingPointEntity getChargingPointEntityById(String id) {
		// TODO Auto-generated method stub
		return chargingPointRepository.findById(id).get();
	}

	@Override
	public ChargingPointEntity getChargingPointEntityByChargingPointId(String connectorName) {
		// TODO Auto-generated method stub
		return chargingPointRepository.findByChargingPointId(connectorName);
	}

}
