package com.scube.chargingstation.service;

import static com.scube.chargingstation.exception.ExceptionType.DUPLICATE_ENTITY;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scube.chargingstation.dto.ChargingPointDto;
import com.scube.chargingstation.dto.ConnectorDto;
import com.scube.chargingstation.dto.incoming.ChargingRequestDto;
import com.scube.chargingstation.dto.incoming.ChargingStationDto;
import com.scube.chargingstation.dto.incoming.UserWalletRequestDto;
import com.scube.chargingstation.dto.mapper.ChargingPointMapper;
import com.scube.chargingstation.dto.mapper.ConnectorMapper;
import com.scube.chargingstation.dto.response.ChargingPointResponse;
import com.scube.chargingstation.dto.response.Response;
import com.scube.chargingstation.entity.ChargingPointEntity;
import com.scube.chargingstation.entity.ChargingRequestEntity;
import com.scube.chargingstation.entity.ConnectorEntity;
import com.scube.chargingstation.entity.UserInfoEntity;
import com.scube.chargingstation.entity.UserWalletDtlEntity;
import com.scube.chargingstation.entity.UserWalletEntity;
import com.scube.chargingstation.exception.BRSException;
import com.scube.chargingstation.exception.EntityType;
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
	UserInfoRepository userInfoRepository;
	
	@Autowired
	UserWalletRepository userWalletRepository;
	
	@Autowired
	UserWalletDtlRepository userWalletDtlRepository;
	
	@Autowired
	ChargingPointService	chargingPointService;
	
	@Autowired
	ConnectorService	connectorService;
	
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
				
			chargingRequestEntity.setChargingPointEntity(chargingPointEntity);
			chargingRequestEntity.setConnectorEntity(connectorEntity);
			chargingRequestEntity.setUserInfoEntity(userInfoEntity);

			
			chargingRequestEntity.setStatus(chargingRequestDto.getStatus());
			chargingRequestEntity.setRequestAmount(chargingRequestDto.getRequestAmount());
			chargingRequestEntity.setIsdeleted("N");
			
			chargingRequestRepository.save(chargingRequestEntity);
			
			//resp="success";  test
			
			//temp chnge  
			
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
	public boolean processWalletMoney(UserWalletRequestDto userWalletRequestDto) {
		// TODO Auto-generated method stub
		
		
		UserWalletDtlEntity userWalletDtlEntity=new UserWalletDtlEntity();
		ChargingRequestEntity crEntity=null;
		
		UserInfoEntity userInfoEntity = userInfoRepository.findByMobilenumber(userWalletRequestDto.getMobileUser_Id());
		
		if(userInfoEntity==null) { 
			throw BRSException.throwException("Error: User does not exist"); 
		}
		
		if((userWalletRequestDto.getChargeRequestId()!=null)&&(!userWalletRequestDto.getChargeRequestId().isEmpty()))
		{
		Optional<ChargingRequestEntity> chargingRequestEntity=chargingRequestRepository.findById(Long.parseLong(userWalletRequestDto.getChargeRequestId()));
		crEntity = chargingRequestEntity.get();
		}
		
		userWalletDtlEntity.setTransactionType(userWalletRequestDto.getTraansactionType());
		userWalletDtlEntity.setUserInfoEntity(userInfoEntity);
		userWalletDtlEntity.setChargingRequestEntity(crEntity);
		userWalletDtlEntity.setAmount(userWalletRequestDto.getRequestAmount());
		
	  //save/update user wallet
			Double balance=0.0;
			UserWalletEntity userWaltEntity=new UserWalletEntity();
		
			UserWalletEntity userchkWaltEntity=userWalletRepository.findByUserInfoEntity(userInfoEntity);
		 
		 
			if(userchkWaltEntity!=null)
				userWaltEntity=userchkWaltEntity;
			
	//	 Optional<UserWalletEntity> userWalletEntity=userWalletRepository.findBy

		
		// if(userWalletEntity!=null)
		// userWaltEntity=userWalletEntity.get();
		
		 
		 Double amount=Double.parseDouble(userWalletRequestDto.getRequestAmount());
		 
		 UserWalletEntity userbalWaltEntity=userWalletRepository.findBalanceByUserId(userInfoEntity.getId());
		 String currentBal="0";
		 
		 if(userbalWaltEntity!=null)
			 currentBal=userbalWaltEntity.getCurrentBalance();
		 
		 Double curBal=Double.parseDouble(currentBal);
		 
		  if(userWalletRequestDto.getTraansactionType().equals("Credit"))
		  {
			  balance=curBal+amount;
		  } 
		  else if(userWalletRequestDto.getTraansactionType().equals("Debit")) 
		  {
			if(curBal<amount)
				 { 
					throw BRSException.throwException("Error: Insufficient balance"); 
				}
			  
			  
		  balance=curBal-amount;
		  } 
		  
		  userWaltEntity.setUserInfoEntity(userInfoEntity);
		  userWaltEntity.setCurrentBalance(balance.toString());
		  userWalletRepository.save(userWaltEntity);
		//
		
		
		userWalletDtlRepository.save(userWalletDtlEntity);
		
		return true;
	}

	@Override
	public Map<String, String> getMyWalletBalance(UserWalletRequestDto userWalletRequestDto) {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap<String, String>();
		String balance="0";

		
		UserInfoEntity userInfoEntity = userInfoRepository.findByMobilenumber(userWalletRequestDto.getMobileUser_Id());
		
		
		if(userInfoEntity==null) { 
			throw BRSException.throwException("Error: User does not exist"); 
		}

		UserWalletEntity userWalletEntity = userWalletRepository.findByUserInfoEntity(userInfoEntity);
			if(userWalletEntity!=null)
			balance=userWalletEntity.getCurrentBalance();
		
		map.put("balance", balance);
		
		
		return map;
	}

	@Override
	public List<ChargingPointResponse> getNearByChargingStations11(ChargingStationDto chargingStationDto) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
