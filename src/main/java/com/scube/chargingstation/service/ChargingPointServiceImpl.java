package com.scube.chargingstation.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import org.hibernate.validator.internal.engine.messageinterpolation.ParameterTermResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scube.chargingstation.dto.AmenityDto;
import com.scube.chargingstation.dto.ChargingHistoryRespDto;
import com.scube.chargingstation.dto.ChargingPointDto;
import com.scube.chargingstation.dto.incoming.ChargingPointIncomingDto;
import com.scube.chargingstation.dto.incoming.ConnectorsIncomingDto;
import com.scube.chargingstation.dto.mapper.ChargingHistoryMapper;
import com.scube.chargingstation.dto.mapper.ChargingPointMapper;
import com.scube.chargingstation.entity.AmenitiesEntity;
import com.scube.chargingstation.entity.ChargepointForSeverEntity;
import com.scube.chargingstation.entity.ChargingPointConnectorRateEntity;
import com.scube.chargingstation.entity.ChargingPointEntity;
import com.scube.chargingstation.entity.ConnectorEntity;
import com.scube.chargingstation.entity.ConnectorStatusEntity;
import com.scube.chargingstation.entity.PartnerInfoEntity;
import com.scube.chargingstation.entity.RoleEntity;
import com.scube.chargingstation.entity.UserInfoEntity;
import com.scube.chargingstation.exception.BRSException;
import com.scube.chargingstation.exception.EntityType;
import com.scube.chargingstation.exception.ExceptionType;
import com.scube.chargingstation.repository.ChargepointForSeverRepository;
import com.scube.chargingstation.repository.ChargingPointConnectorRateRepository;
import com.scube.chargingstation.repository.ChargingPointRepository;
import com.scube.chargingstation.repository.ConnectorStatusRepository;
import com.scube.chargingstation.repository.RoleRepository;
import com.scube.chargingstation.util.CheckChargerStatus;

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
	
	
	@Autowired
	ChargepointForSeverRepository chargepointForSeverRepository; 
	
	@Autowired
	ConnectorStatusRepository connectorStatusRepository;
	
	@Autowired
	ChargingPointConnectorRateRepository chargingPointConnectorRateRepository;
	
	@Autowired
	PartnerService partnerService;
	
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	RoleRepository roleRepository;
	
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

		
		ChargingPointEntity	chargingPointDuplicateEntity = chargingPointRepository.findByChargingPointId(chargingPointIncomingDto.getChargingPointId());
		
		if(chargingPointDuplicateEntity != null) {
			
			 throw BRSException.throwException(EntityType.CHARGINGSTATION, ExceptionType.DUPLICATE_ENTITY , chargingPointIncomingDto.getChargingPointId()); 
		}
		
		System.out.println("==============asasas======================"+chargingPointIncomingDto.getPartnerId());
		
		
		  PartnerInfoEntity partnerInfoEntity = partnerService.getPartnerById(chargingPointIncomingDto.getPartnerId());
		  
		  if(partnerInfoEntity == null) {
		  
			  throw BRSException.throwException(EntityType.CHARGINGSTATION,ExceptionType.ENTITY_NOT_FOUND , chargingPointIncomingDto.getChargingPointId());
		  
		  }
		 
		
		chargingPointEntity.setName(chargingPointIncomingDto.getName());
		chargingPointEntity.setChargingPointId(chargingPointIncomingDto.getChargingPointId());
		chargingPointEntity.setPartnerName(chargingPointIncomingDto.getPartnerName());
		chargingPointEntity.setPartner(partnerInfoEntity);
		chargingPointEntity.setStartTime(chargingPointIncomingDto.getStartTime());
		chargingPointEntity.setEndTime(chargingPointIncomingDto.getEndTime());
		chargingPointEntity.setAddress(chargingPointIncomingDto.getAddress());
		chargingPointEntity.setAddress2(chargingPointIncomingDto.getAddress2());
		chargingPointEntity.setPincode(chargingPointIncomingDto.getPincode());
		chargingPointEntity.setLatitude(chargingPointIncomingDto.getLatitude());
		chargingPointEntity.setLongitude(chargingPointIncomingDto.getLongitude());
//		chargingPointEntity.setStatus(chargingPointIncomingDto.getStatus());
		chargingPointEntity.setStatus("CLOSE");
		chargingPointEntity.setManufractures(chargingPointIncomingDto.getManufractures());
		chargingPointEntity.setCommunicationtype(chargingPointIncomingDto.getCommunicationtype());
		chargingPointEntity.setPowerstandards(chargingPointIncomingDto.getPowerstandards());
		chargingPointEntity.setStationtype(chargingPointIncomingDto.getStationtype());
		chargingPointEntity.setRating(0.0);
		chargingPointEntity.setIsdeleted("N");
		
	    Set<ConnectorsIncomingDto> connectors =  chargingPointIncomingDto.getConnectors();
	    
	    
	    ChargepointForSeverEntity	chargepointForSeverEntity = new ChargepointForSeverEntity();
	    
	    chargepointForSeverEntity.setChargePointId(chargingPointIncomingDto.getChargingPointId());
	    chargepointForSeverEntity.setName(chargingPointIncomingDto.getName());
	    
	    chargepointForSeverRepository.save(chargepointForSeverEntity);
	    
	    
	    for(ConnectorsIncomingDto connectorDto : connectors) {
	    	
	    	ConnectorEntity  connectorEntity = new ConnectorEntity();
	    	ConnectorStatusEntity connectorStatusEntity = new ConnectorStatusEntity(); 	
	    	
	    	connectorEntity.setConnectorId(connectorDto.getConnectorId());
	    	connectorEntity.setChargerTypeEntity(connectorTypeService.getChargerTypeEntityByName(connectorDto.getName()));
	    	connectorEntity.setConnectorSlotIntervalTime(connectorDto.getSlotIntervalTime());
	    	connectorEntity.setIsdeleted("N");
			
			  connectorStatusEntity.setConnectorId(Integer.parseInt(connectorDto.getConnectorId()));
			  connectorStatusEntity.setChargePointId(chargingPointIncomingDto.getChargingPointId());
			  connectorStatusEntity.setLastStatus("Available");
			  
			  ConnectorStatusEntity connectorStatusEntitya = connectorStatusRepository.save(connectorStatusEntity);
			  
			  connectorEntity.setConnectorStatusEntity(connectorStatusEntitya);
			    	
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
		
		List<ChargingPointEntity>	chargingPointEntity = chargingPointRepository.findAll();
			
		 
		return ChargingPointMapper.toChargingPointDto(chargingPointEntity);
	}
	
	
	@Override
	public List<ChargingPointDto> getAllChargingStationsByUserMobile(String id) {
		// TODO Auto-generated method stub
		
		List<ChargingPointEntity> chargingPointEntity = null;
		
		UserInfoEntity userInfoEntity = userInfoService.getUserByMobilenumber(id);
		
		if(userInfoEntity.getRole().getNameCode().equals("AU")) {
			
			chargingPointEntity = chargingPointRepository.findAll();
			
		}
		
		if(userInfoEntity.getRole().getNameCode().equals("PU")) {
			
			chargingPointEntity = chargingPointRepository.findChargingStationsByPartnerId(userInfoEntity.getPartner().getId());
		}
		
		// role  		if 
		// admin 		 chargingPointEntity =	chargingPointRepository.findAll(); 
		 
		// PU		else 
		 
		return ChargingPointMapper.toChargingPointDto(chargingPointEntity);
	}
	
	@Override
	public List<ChargingPointEntity> getAllChargingPointEntity() {
		// TODO Auto-generated method stub
		return chargingPointRepository.findAll();
		
	
		
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
	public Boolean getChargingStationsgoLiveCheckById(String id) {
		// TODO Auto-generated method stub
		
		ChargingPointEntity chargingPointEntity = chargingPointRepository.findById(id).get();
		
		if(chargingPointEntity == null) {
			throw BRSException.throwException(EntityType.CHARGINGSTATION, ExceptionType.VALUE_NOT_FOUND , chargingPointEntity.getChargingPointId()); 
		}
		
		
		Set<ConnectorEntity> connectorEntities =  chargingPointEntity.getConnectorEntities(); 
		
		if(connectorEntities.size() == 0) {
			
			throw BRSException.throwException(EntityType.CHARGINGSTATION, ExceptionType.ENTITY_NOT_FOUND); 
		}
		
		
		for(ConnectorEntity connectorEntity : connectorEntities ) {
			
			
					
			String response=CheckChargerStatus.callResetConnectorApi(chargingPointEntity.getChargingPointId(),connectorEntity.getConnectorId());
			
			if(!response.equals("OK"))
	  		{
				throw BRSException.throwException(EntityType.CHARGINGPOINTCONNECTOR, ExceptionType.VALUE_NOT_LIVE , connectorEntity.getConnectorId()); 

	  		}
			
			String res=CheckChargerStatus.callChangeConfiguration(chargingPointEntity.getChargingPointId());
			
			if(!res.equals("OK"))
	  		{
				throw BRSException.throwException(EntityType.CHARGINGPOINTCONNECTOR, ExceptionType.FREQUENCY_NOT_FOUND , chargingPointEntity.getChargingPointId()); 

	  		}
			
			ChargingPointConnectorRateEntity chargingPointConnectorRateEntity = chargingPointConnectorRateRepository.findByChargingPointEntityAndConnectorEntityAndStatusGroupByWithLimit(chargingPointEntity.getId(),connectorEntity.getId(),"ACTIVE");
			
		    if(chargingPointConnectorRateEntity == null) {
				throw BRSException.throwException(EntityType.CHARGINGPOINTCONNECTORRATE, ExceptionType.VALUE_NOT_FOUND ,  connectorEntity.getConnectorId()); 
			}
		    
		    
		}
	    
	    ChargepointForSeverEntity	chargepointForSeverEntity = chargepointForSeverRepository.findByChargePointId(chargingPointEntity.getChargingPointId());
	    
	    if(chargepointForSeverEntity == null) {
			throw BRSException.throwException(EntityType.CONNECTORSTATUS, ExceptionType.CONTACT_ADMINISTRATOR); 
		}
	    
	    List<ConnectorStatusEntity> connectorStatusEntitya = connectorStatusRepository.findByChargePointId(chargingPointEntity.getChargingPointId());
		
	    if(connectorStatusEntitya.size() == 0) {
			throw BRSException.throwException(EntityType.CONNECTORSTATUS, ExceptionType.VALUE_NOT_FOUND , chargingPointEntity.getChargingPointId()); 
		}
	    
	    
	    
	    
	    chargingPointEntity.setStatus("OPEN");
	    
	    chargingPointRepository.save(chargingPointEntity);
	    
		return true;
	}

	@Override
	public Boolean updateChargingPoint(@Valid ChargingPointIncomingDto chargingPointIncomingDto) {
		// TODO Auto-generated method stub
		
		Set<ConnectorEntity> connectorEntities = new HashSet<ConnectorEntity>() ;
		
		Set<AmenitiesEntity> amenitiesEntities = new HashSet<AmenitiesEntity>();
		
		ChargingPointEntity chargingPointEntity = chargingPointRepository.findById(chargingPointIncomingDto.getId()).get();
		
		PartnerInfoEntity partnerInfoEntity = partnerService.getPartnerById(chargingPointIncomingDto.getPartnerId());

		// null	
		if(partnerInfoEntity == null) {
			
			throw BRSException.throwException(EntityType.CHARGINGSTATION, ExceptionType.ENTITY_NOT_FOUND , chargingPointIncomingDto.getChargingPointId()); 
		
		}
		
		
		chargingPointEntity.setName(chargingPointIncomingDto.getName());
//		chargingPointEntity.setChargingPointId(chargingPointIncomingDto.getChargingPointId());
		chargingPointEntity.setPartner(partnerInfoEntity);
		chargingPointEntity.setStartTime(chargingPointIncomingDto.getStartTime());
		chargingPointEntity.setEndTime(chargingPointIncomingDto.getEndTime());
		chargingPointEntity.setAddress(chargingPointIncomingDto.getAddress());
		chargingPointEntity.setAddress2(chargingPointIncomingDto.getAddress2());
		chargingPointEntity.setPincode(chargingPointIncomingDto.getPincode());
		chargingPointEntity.setLatitude(chargingPointIncomingDto.getLatitude());
		chargingPointEntity.setLongitude(chargingPointIncomingDto.getLongitude());
//		chargingPointEntity.setStatus(chargingPointIncomingDto.getStatus());
		chargingPointEntity.setStatus("CLOSE");
		chargingPointEntity.setManufractures(chargingPointIncomingDto.getManufractures());
		chargingPointEntity.setCommunicationtype(chargingPointIncomingDto.getCommunicationtype());
		chargingPointEntity.setPowerstandards(chargingPointIncomingDto.getPowerstandards());
		chargingPointEntity.setStationtype(chargingPointIncomingDto.getStationtype());
		
		
	    Set<ConnectorsIncomingDto> connectors =  chargingPointIncomingDto.getConnectors();
	    
	    for(ConnectorsIncomingDto connectorDto : connectors) {
	    	
	    	ConnectorEntity  connectorEntity = null;
	    	
	    	if(connectorDto.getId() != null){
	    		connectorEntity = connectorService.getConnectorEntityById(connectorDto.getId());
	    	}else {
	    		connectorEntity = new ConnectorEntity();
	    	}
	    	ConnectorStatusEntity connectorStatusEntity = new ConnectorStatusEntity(); 	
	    	
	    	connectorEntity.setConnectorId(connectorDto.getConnectorId());
	    	connectorEntity.setChargerTypeEntity(connectorTypeService.getChargerTypeEntityByName(connectorDto.getName()));
	    	connectorEntity.setConnectorSlotIntervalTime(connectorDto.getSlotIntervalTime());
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

	
	public PartnerInfoEntity findByPartnerId(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ChargingPointDto> getNearByChargingPoints(Double lat,Double longi) {
		// TODO Auto-generated method stub
		
		
		List<Map<String, String>> listDtl=chargingPointRepository.getNearByChargingStations(lat, longi);
		List<ChargingPointDto> chargingPointDtoLst = ChargingPointMapper.toChargingPointDtoLst(listDtl);
		
		return chargingPointDtoLst;
	}

	@Override
	public ChargingPointEntity getChargingPointEntityByChargePointId(String chargingPointId) {
		// TODO Auto-generated method stub
		return chargingPointRepository.findChargingPointEntityByChargingPointId(chargingPointId);
	}


}
