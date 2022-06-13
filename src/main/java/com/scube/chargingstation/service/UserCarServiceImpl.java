   package com.scube.chargingstation.service;

import static com.scube.chargingstation.exception.ExceptionType.ALREADY_EXIST;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.firebase.messaging.Notification;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.scube.chargingstation.controller.AuthController;
import com.scube.chargingstation.dto.CarModelDto;
import com.scube.chargingstation.dto.ChargingPointDto;
import com.scube.chargingstation.dto.ChargingPointConnectorDto;
import com.scube.chargingstation.dto.UserCarRespDto;
import com.scube.chargingstation.dto.incoming.ChargingRequestDto;
import com.scube.chargingstation.dto.incoming.ChargingStationDto;
import com.scube.chargingstation.dto.incoming.NotificationReqDto;
import com.scube.chargingstation.dto.incoming.UserCarDto;
import com.scube.chargingstation.dto.incoming.UserWalletRequestDto;
import com.scube.chargingstation.dto.mapper.CarModelMapper;
import com.scube.chargingstation.dto.mapper.ChargingPointMapper;
import com.scube.chargingstation.dto.mapper.ConnectorMapper;
import com.scube.chargingstation.dto.mapper.RoleMapper;
import com.scube.chargingstation.dto.mapper.UserCarMapper;
import com.scube.chargingstation.dto.response.Response;
import com.scube.chargingstation.entity.AmenitiesEntity;
import com.scube.chargingstation.entity.CarModelEntity;
import com.scube.chargingstation.entity.ChargerTypeEntity;
import com.scube.chargingstation.entity.ChargingPointEntity;
import com.scube.chargingstation.entity.ChargingRequestEntity;
import com.scube.chargingstation.entity.ConnectorEntity;
import com.scube.chargingstation.entity.UserCarsEntity;
import com.scube.chargingstation.entity.UserInfoEntity;  
import com.scube.chargingstation.entity.UserWalletDtlEntity;
import com.scube.chargingstation.entity.UserWalletEntity;
import com.scube.chargingstation.exception.BRSException;
import com.scube.chargingstation.exception.EntityType;
import com.scube.chargingstation.repository.CarModelRepository;
import com.scube.chargingstation.repository.ChargerTypeRepository;
import com.scube.chargingstation.repository.ChargingPointRepository;
import com.scube.chargingstation.repository.ChargingRequestRepository;
import com.scube.chargingstation.repository.UserCarsRepository;
import com.scube.chargingstation.repository.UserInfoRepository;
import com.scube.chargingstation.repository.UserWalletDtlRepository;
import com.scube.chargingstation.repository.UserWalletRepository;

@Service
public class UserCarServiceImpl implements UserCarService {

	@Autowired
	CarModelRepository carModelRepository;
	
	@Autowired
	UserInfoRepository userInfoRepository;
	
	@Autowired
	UserCarsRepository userCarsRepository;
	
  	private static final Logger logger = LoggerFactory.getLogger(UserCarServiceImpl.class);


	@Override
	public boolean addUserCars(UserCarDto userCarDto) {

		UserCarsEntity userCarsEntity=new UserCarsEntity();
		
		UserInfoEntity userInfoEntity = userInfoRepository.findByMobilenumber(userCarDto.getMobileUser_Id());
		if(userInfoEntity==null)
		{   
			throw BRSException.throwException("Error: User does not exist"); 
		}
		
		Optional<CarModelEntity> carModelEntity=carModelRepository.findById(userCarDto.getModelId());
		CarModelEntity crEntity = carModelEntity.get();
		
		if(carModelEntity==null)
		{
			throw BRSException.throwException("Error: Vehicle Model does not exist"); 
		}
		
		userCarsEntity.setUserInfoEntity(userInfoEntity);
		userCarsEntity.setCarModelEntity(crEntity);
		userCarsEntity.setVehicleNo(userCarDto.getVehicleNo());
		userCarsEntity.setIsdeleted("N");
		userCarsRepository.save(userCarsEntity);
		
		return true;
	}


	@Override
	public List<UserCarRespDto> getUserCars(String mobileUser_Id) {
		// TODO Auto-generated method stub
		
		UserInfoEntity userInfoEntity = userInfoRepository.findByMobilenumber(mobileUser_Id);
		if(userInfoEntity==null)
		{
			throw BRSException.throwException("Error: User does not exist"); 
		}
		
		logger.info("***getUserCars***");
		
		List<UserCarsEntity> userCarsEntity=userCarsRepository.findByUserInfoEntityAndIsdeleted(userInfoEntity, "N");
		//return null;
		return UserCarMapper.toUserCarDto(userCarsEntity);
	}


	@Override
	public Object removeUserCar(@Valid UserCarDto userCarDto) {
		// TODO Auto-generated method stub
		
		if(userCarDto.getId()==null||userCarDto.getId()=="")
		{
			throw BRSException.throwException("Error: Invalid User Car to remove"); 
		}
		Optional<UserCarsEntity> userCarsEntity=userCarsRepository.findById(userCarDto.getId());
		UserCarsEntity entity=userCarsEntity.get();
		
		if(entity==null)
		{
			throw BRSException.throwException("Error: Invalid User Car to remove"); 
		}
		
		int flg=userCarsRepository.removeUserCar(userCarDto.getId());
		return true;
	}

}

