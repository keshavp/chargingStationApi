package com.scube.chargingstation.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.scube.chargingstation.controller.AuthController;
import com.scube.chargingstation.dto.ChargingPointDto;
import com.scube.chargingstation.dto.incoming.ChargingRequestDto;
import com.scube.chargingstation.dto.incoming.ChargingStationDto;
import com.scube.chargingstation.dto.mapper.ChargingPointMapper;
import com.scube.chargingstation.entity.ChargingPointEntity;
import com.scube.chargingstation.entity.ChargingRequestEntity;
import com.scube.chargingstation.entity.ConnectorEntity;
import com.scube.chargingstation.entity.UserInfoEntity;
import com.scube.chargingstation.entity.UserWalletEntity;
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
	 
	private static final Logger logger = LoggerFactory.getLogger(ChargingRequestServiceImpl.class);

	 
	
	@Override
	public boolean addChargingRequest(ChargingRequestDto chargingRequestDto) {
			
			ChargingRequestEntity chargingRequestEntity  = new  ChargingRequestEntity();
			String connectorId= Integer.toString(chargingRequestDto.getConnectorId());
			
			UserInfoEntity userInfoEntity = userInfoRepository.findByMobilenumber(chargingRequestDto.getMobileUser_Id());
			if(userInfoEntity==null)
			{
				throw BRSException.throwException("Error: User does not exist"); 
			}
			
			ChargingPointEntity	chargingPointEntity = chargingPointService.getChargingPointEntityByChargingPointId(chargingRequestDto.getChargePointId());
			if(chargingPointEntity==null)
			{
				throw BRSException.throwException("Error: Charging Point does not exist"); 
			}
			
			
			ConnectorEntity	connectorEntity = connectorService.getConnectorEntityById(connectorId) ;
			if(connectorEntity==null)
			{
				throw BRSException.throwException("Error: Connector does not exist"); 
			}
			
			
			UserWalletEntity userWalletEntity=userWalletRepository.findByUserInfoEntity(userInfoEntity);
			if(userWalletEntity==null)
			{
				  throw BRSException.throwException("Error: Insufficient Wallet Balance");
			}
				
			Double balance=0.0;
			String curBal=userWalletEntity.getCurrentBalance(); //existing balance
			Double dCurBal=Double.parseDouble(curBal); //double existing balance
			
			Double reqAmt=Double.parseDouble(chargingRequestDto.getRequestAmount()); //requested charging amount
			if(dCurBal<reqAmt)
			{
				  throw BRSException.throwException("Error: Insufficient Wallet Balance");
			}
			
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
			
			chargingRequestEntity.setCustName(chargingRequestDto.getName());
			chargingRequestEntity.setMobileNo(chargingRequestDto.getMobileNo());
			chargingRequestEntity.setVehicleNO(chargingRequestDto.getVechicleNo());
			
			
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
