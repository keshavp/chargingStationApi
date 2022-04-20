package com.scube.chargingstation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.scube.chargingstation.dto.ChargingPointDto;
import com.scube.chargingstation.dto.incoming.ChargingRequestDto;
import com.scube.chargingstation.dto.incoming.ChargingStationDto;
import com.scube.chargingstation.dto.mapper.ChargingPointMapper;
import com.scube.chargingstation.entity.ChargingPointEntity;
import com.scube.chargingstation.entity.ChargingRequestEntity;
import com.scube.chargingstation.entity.ConnectorEntity;
import com.scube.chargingstation.entity.UserInfoEntity;
import com.scube.chargingstation.exception.BRSException;
import com.scube.chargingstation.repository.ChargerTypeRepository;
import com.scube.chargingstation.repository.ChargingPointRepository;
import com.scube.chargingstation.repository.ChargingRequestRepository;
import com.scube.chargingstation.repository.UserInfoRepository;
import com.scube.chargingstation.repository.UserWalletDtlRepository;
import com.scube.chargingstation.repository.UserWalletRepository;

@Service
public class ChargingRequestServiceImpl implements ChargingRequestService {

	@Autowired
	ChargingPointRepository chargingPointRepository;
	
	@Autowired
	ChargingRequestRepository chargingRequestRepository;
	
	@Autowired
	ChargerTypeRepository chargerTypeRepository;
	
	@Autowired
	UserInfoRepository userInfoRepository;
	
	@Autowired
	UserWalletRepository userWalletRepository;
	
	@Autowired
	UserWalletDtlRepository userWalletDtlRepository;
	
	@Autowired
	ChargingPointService	chargingPointService;
	
	@Autowired
	ConnectorService	connectorService;
	
	
	 @Value("${chargingstation.chargertype}")
	private   String imgLocation;
	
	@Override
	public boolean addChargingRequest(ChargingRequestDto chargingRequestDto) {
			
			ChargingRequestEntity chargingRequestEntity  = new  ChargingRequestEntity();
			

		/*
		 * if(chargingRequestDto.getRequestAmount() == 100 ||
		 * chargingRequestDto.getRequestAmount() == 500 ||
		 * chargingRequestDto.getRequestAmount() == 1000) { logger.
		 * error("throw error that Mobilenumber already exists for Mobilenumber = "+
		 * userInfoIncomingDto.getMobilenumber()); throw
		 * BRSException.throwException(EntityType.USER, DUPLICATE_ENTITY,
		 * userInfoIncomingDto.getMobilenumber()); }
		 */
			
			
			String connectorId= Integer.toString(chargingRequestDto.getConnectorId());
			
			ChargingPointEntity	chargingPointEntity = chargingPointService.getChargingPointEntityByChargingPointId(chargingRequestDto.getChargePointId());
			ConnectorEntity	connectorEntity = connectorService.getConnectorEntityById(connectorId) ;
			UserInfoEntity userInfoEntity = userInfoRepository.findByMobilenumber(chargingRequestDto.getMobileUser_Id());
			
			
		/*
		 * List<ChargingRequestEntity> list=chargingRequestRepository.
		 * findByChargingPointEntityAndConnectorEntityAndUserInfoEntityAndChargingStatus
		 * (chargingPointEntity, connectorEntity, userInfoEntity, "Starting");
		 */
		  
		
		  List<ChargingRequestEntity> list=chargingRequestRepository.findMyOnGoingChargingProcesses(chargingPointEntity, connectorEntity, userInfoEntity);
				  
		  
		  	if(list.size()>0)
			{
			  throw BRSException.throwException("Error: Can't book, charging is already in process");
			}
		  
		 
			chargingRequestEntity.setChargingPointEntity(chargingPointEntity);
			chargingRequestEntity.setConnectorEntity(connectorEntity);
			chargingRequestEntity.setUserInfoEntity(userInfoEntity);
			chargingRequestEntity.setStatus(chargingRequestDto.getStatus());
			chargingRequestEntity.setRequestAmount(Double.valueOf(chargingRequestDto.getRequestAmount()));
			chargingRequestEntity.setChargingStatus("Pending");
			chargingRequestEntity.setIsdeleted("N");
			
			
			chargingRequestRepository.save(chargingRequestEntity);
			
			return true;
			
		}

	@Override
	public List<ChargingRequestEntity> findChargingRequestEntityByChargingStatus(String chargingStatus) {
		// TODO Auto-generated method stub
		
		return chargingRequestRepository.findByChargingStatus("Starting");
	}

	@Override
	public ChargingRequestEntity findChargingRequestEntityByChargingPointEntityAndConnectorEntityAndStatus(
			ChargingPointEntity chargingPointEntity, ConnectorEntity connectorEntity, String string) {
		// TODO Auto-generated method stub
		
		return chargingRequestRepository.findByChargingPointEntityAndConnectorEntityAndStatus(chargingPointEntity, connectorEntity,"REQUESTED");
	}
	
	
	
	
	

	@Override
	public List<ChargingPointDto> getNearByChargingStations(ChargingStationDto chargingStationDto) {
		// TODO Auto-generated method stub
	

		
		List<ChargingPointEntity> cpEntity=chargingPointRepository.findAll();
		List<ChargingPointDto> chargingPointDto = ChargingPointMapper.toChargingPointDto(cpEntity);
		
		return chargingPointDto;
		
	}
	
	
}
