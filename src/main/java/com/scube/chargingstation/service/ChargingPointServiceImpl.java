package com.scube.chargingstation.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scube.chargingstation.dto.AmenityDto;
import com.scube.chargingstation.dto.ChargingPointDto;
import com.scube.chargingstation.dto.ConnectorTypeDto;
import com.scube.chargingstation.dto.incoming.ChargingPointIncomingDto;
import com.scube.chargingstation.dto.incoming.ConnectorsIncomingDto;
import com.scube.chargingstation.dto.ChargingPointConnectorDto;
import com.scube.chargingstation.dto.mapper.ChargingPointMapper;
import com.scube.chargingstation.entity.AmenitiesEntity;
import com.scube.chargingstation.entity.ChargingPointEntity;
import com.scube.chargingstation.entity.ConnectorEntity;
import com.scube.chargingstation.entity.ConnectorStatusEntity;
import com.scube.chargingstation.repository.ChargingPointRepository;

@Service
public class ChargingPointServiceImpl implements ChargingPointService {

	@Autowired
	ChargingPointRepository chargingPointRepository;
	
	@Autowired
	AmenitiesService amenitiesService;
	
	@Autowired
	ConnectorTypeService connectorTypeService;
	
	@Autowired
	ConnectorService connectorService;
	
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

	@Override
	public Boolean addChargingPoint(@Valid ChargingPointIncomingDto chargingPointIncomingDto) {
		// TODO Auto-generated method stub
		
		ChargingPointEntity	chargingPointEntity = new ChargingPointEntity();
		
		Set<ConnectorEntity> connectorEntities = new HashSet<ConnectorEntity>() ;
		
		Set<AmenitiesEntity> amenitiesEntities = new HashSet<AmenitiesEntity>();
		
		chargingPointEntity.setName(chargingPointIncomingDto.getName());
		chargingPointEntity.setChargingPointId(chargingPointIncomingDto.getChargingPointId());
		chargingPointEntity.setPartnerName(chargingPointIncomingDto.getPartnerName());
		chargingPointEntity.setStartTime(chargingPointIncomingDto.getStartTime());
		chargingPointEntity.setEndTime(chargingPointIncomingDto.getEndTime());
		chargingPointEntity.setAddress(chargingPointIncomingDto.getAddress());
		chargingPointEntity.setAddress2(chargingPointIncomingDto.getAddress2());
		chargingPointEntity.setPincode(chargingPointIncomingDto.getPincode());
		chargingPointEntity.setLatitude(chargingPointIncomingDto.getLatitude());
		chargingPointEntity.setLongitude(chargingPointIncomingDto.getLongitude());
		chargingPointEntity.setStatus(chargingPointIncomingDto.getStatus());
		chargingPointEntity.setManufractures(chargingPointIncomingDto.getManufractures());
		chargingPointEntity.setCommunicationtype(chargingPointIncomingDto.getCommunicationtype());
		chargingPointEntity.setPowerstandards(chargingPointIncomingDto.getPowerstandards());
		
		
	    Set<ConnectorsIncomingDto> connectors =  chargingPointIncomingDto.getConnectors();
	    
	    for(ConnectorsIncomingDto connectorDto : connectors) {
	    	
	    	ConnectorEntity  connectorEntity = new ConnectorEntity();
	    	ConnectorStatusEntity connectorStatusEntity = new ConnectorStatusEntity(); 	
	    	
	    	connectorEntity.setConnectorId(connectorDto.getConnectorId());
	    	connectorEntity.setChargerTypeEntity(connectorTypeService.getChargerTypeEntityByName(connectorDto.getName()));
	    	
			/*
			 * connectorStatusEntity.setConnectorId(Integer.parseInt(connectorDto.
			 * getConnectorId()));
			 * connectorStatusEntity.setChargePointId(chargingPointIncomingDto.
			 * getChargingPointId());
			 * 
			 * connectorEntity.setConnectorStatusEntity(connectorStatusEntity);
			 */   	
	    	connectorEntities.add(connectorEntity);
	    }
	    
	    chargingPointEntity.setConnectorEntities(connectorEntities);
	    
	    Set<AmenityDto> Amenities = chargingPointIncomingDto.getAmenities();
		
	    for(AmenityDto amenityDto : Amenities) {
	    	amenitiesEntities.add(amenitiesService.findAmenitiesEntityByName(amenityDto.getName()));
	    }
	    
	    chargingPointEntity.setAmenities(amenitiesEntities);
	    
		
	    chargingPointRepository.save(chargingPointEntity);
	    
		return true;
	}

	@Override
	public List<ChargingPointDto> getAllChargingStations() {
		// TODO Auto-generated method stub
		List<ChargingPointEntity> chargingPointEntity =	chargingPointRepository.findAll();
		return ChargingPointMapper.toChargingPointDto(chargingPointEntity);
	}
	
	@Override
	public List<ChargingPointDto> getAllActiveChargingStations() {
		// TODO Auto-generated method stub
		List<ChargingPointEntity> chargingPointEntity =	chargingPointRepository.findByIsdeleted("N");
		return ChargingPointMapper.toChargingPointDto(chargingPointEntity);
	}
	
	@Override
	public ChargingPointDto getChargingStationsById(String id) {
		// TODO Auto-generated method stub
		ChargingPointEntity chargingPointEntity = chargingPointRepository.findById(id).get();
		return ChargingPointMapper.toChargingPointDto(chargingPointEntity);
	}

	@Override
	public Boolean updateChargingPoint(@Valid ChargingPointIncomingDto chargingPointIncomingDto) {
		// TODO Auto-generated method stub
		return null;
	}

	

	

}
