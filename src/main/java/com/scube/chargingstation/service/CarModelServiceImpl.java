  package com.scube.chargingstation.service;

  import static com.scube.chargingstation.exception.ExceptionType.ALREADY_EXIST;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Paths;
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
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.firebase.messaging.Notification;
import com.google.common.io.Files;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.scube.chargingstation.controller.AuthController;
import com.scube.chargingstation.dto.CarModelDto;
import com.scube.chargingstation.dto.ChargerTypeDto;
import com.scube.chargingstation.dto.ChargingPointDto;
import com.scube.chargingstation.dto.ChargingPointConnectorDto;
import com.scube.chargingstation.dto.incoming.CarModelIncomingDto;
import com.scube.chargingstation.dto.incoming.ChargingRequestDto;
import com.scube.chargingstation.dto.incoming.ChargingStationDto;
import com.scube.chargingstation.dto.incoming.NotificationReqDto;
import com.scube.chargingstation.dto.incoming.UserWalletRequestDto;
import com.scube.chargingstation.dto.mapper.CarModelMapper;
import com.scube.chargingstation.dto.mapper.ChargingPointMapper;
import com.scube.chargingstation.dto.mapper.ConnectorMapper;
import com.scube.chargingstation.dto.mapper.RoleMapper;
import com.scube.chargingstation.dto.response.Response;
import com.scube.chargingstation.entity.AmenitiesEntity;
import com.scube.chargingstation.entity.CarModelEntity;
import com.scube.chargingstation.entity.ChargerTypeEntity;
import com.scube.chargingstation.entity.ChargingPointEntity;
import com.scube.chargingstation.entity.ChargingRequestEntity;
import com.scube.chargingstation.entity.ConnectorEntity;
import com.scube.chargingstation.entity.UserInfoEntity;
import com.scube.chargingstation.entity.UserWalletDtlEntity;
import com.scube.chargingstation.entity.UserWalletEntity;
import com.scube.chargingstation.exception.BRSException;
import com.scube.chargingstation.exception.EntityType;
import com.scube.chargingstation.repository.CarModelRepository;
import com.scube.chargingstation.repository.ChargerTypeRepository;
import com.scube.chargingstation.repository.ChargingPointRepository;
import com.scube.chargingstation.repository.ChargingRequestRepository;
import com.scube.chargingstation.repository.UserInfoRepository;
import com.scube.chargingstation.repository.UserWalletDtlRepository;
import com.scube.chargingstation.repository.UserWalletRepository;

@Service
public class CarModelServiceImpl implements CarModelService {

	@Autowired
	CarModelRepository carModelRepository;
	@Autowired
	ChargerTypeService chargerTypeService;
	   
  	private static final Logger logger = LoggerFactory.getLogger(CarModelServiceImpl.class);

  	@Override
	public boolean addCarModel(@Valid CarModelIncomingDto carModelIncomigDto) {
		// TODO Auto-generated method stub
		logger.info("********CarModelServiceImpl addCarModel********");
		if(carModelIncomigDto.getModel()==" " )
		{
			throw BRSException.throwException("CarModel can't be blank");
		}
		
		if(carModelIncomigDto.getDescription()==" " )
		{
			throw BRSException.throwException("Car description can't be blank");
		}
			
		
		/*
		 * if(carModelIncomigDto.getStatus()==" " ) { throw
		 * BRSException.throwException("Staus can't be blank"); }
		 */
	/*	if(carModelIncomigDto.getImgPath()==" " )
		{
			throw BRSException.throwException("ImagePath can't be balnk");
		}
		   */
			Set<ChargerTypeEntity> chargerTypeEntities =new HashSet<ChargerTypeEntity>();
		
		for(ChargerTypeDto  chargerTypeDtos : carModelIncomigDto.getChargertypes()) {
				
		  ChargerTypeEntity carModelEntity = chargerTypeService.findById(chargerTypeDtos.getId());
				
		  
		  chargerTypeEntities.add(carModelEntity);
			
				
		}
		if(carModelIncomigDto.getChargertypes().size()<=0)
		{
		
			throw BRSException.throwException("ChargerTypes can't be blank or null");
		}
	
		
		CarModelEntity carModelEntity= new CarModelEntity(); 
		carModelEntity.setModel(carModelIncomigDto.getModel());
		carModelEntity.setDescription(carModelIncomigDto.getDescription());
		 carModelEntity.setChargertypes(chargerTypeEntities);
		carModelEntity.setStatus(carModelIncomigDto.getStatus());
		carModelEntity.setImagePath(carModelIncomigDto.getImgPath());
		carModelEntity.setIsdeleted("N");
		carModelRepository.save(carModelEntity);
		return true;
	}
	@Override
	  public List<CarModelDto> getCarModels() {
		// TODO Auto-generated method stub
		
	 
		List<CarModelEntity> carModellist=carModelRepository.findByStatus("ACTIVE");
		
		return  CarModelMapper.toCarModelDto(carModellist);
		
	}
	@Override
	public boolean editCarModel(@Valid CarModelIncomingDto carModelIncomigDto) {
		// TODO Auto-generated method stub
		 
		if(carModelIncomigDto.getModel()==" " || carModelIncomigDto.getModel()==null)
		{
			throw BRSException.throwException("CarModel can't be blank or null");
		}
		
        if(carModelIncomigDto.getDescription()==" "|| carModelIncomigDto.getDescription()==null)
        {
        	throw BRSException.throwException("Car Description can't be blank or null");
        }
		/*
		 * if(carModelIncomigDto.getStatus()==" "||
		 * carModelIncomigDto.getStatus()==null) { throw
		 * BRSException.throwException("Car Status can't be blank or null"); }
		 */
        Set<ChargerTypeEntity> chargerTypeEntities =new HashSet<ChargerTypeEntity>();
		
		for(ChargerTypeDto  chargerTypeDtos : carModelIncomigDto.getChargertypes()) {
				
		  ChargerTypeEntity carModelEntity = chargerTypeService.findById(chargerTypeDtos.getId());
				
		  
		  chargerTypeEntities.add(carModelEntity);   
			
				
		}
		
	/*    if(carModelIncomigDto.getImgPath()==" " || carModelIncomigDto.getImgPath()==null)
	    {
	    	throw BRSException.throwException("Car charger types can't be blank or null"); 
	    }   */
		 
	    CarModelEntity carModelEntity=carModelRepository.findById(carModelIncomigDto.getId()).get();
	    
	    carModelEntity.setModel(carModelIncomigDto.getModel());
	    carModelEntity.setDescription(carModelIncomigDto.getDescription());
//	    carModelEntity.setStatus(carModelIncomigDto.getStatus());	
	    carModelEntity.setChargertypes(chargerTypeEntities);
	    carModelEntity.setImagePath(carModelIncomigDto.getImgPath());
	    carModelRepository.save(carModelEntity);
		return true;
	}
	@Override
	public boolean deleteCarModel(String id) {
		// TODO Auto-generated method stub
		
		CarModelEntity carModelEntity=carModelRepository.findById(id).get();
		if(carModelEntity.getId()==" "|| carModelEntity.getId()==null)
		{
			throw BRSException.throwException("Car Model id can't be blank");
		}
		
//		carModelEntity.setIsdeleted("Y");
		carModelRepository.delete(carModelEntity);
		return true;
	}
	
	public CarModelEntity findCarModelById(String id)
	{
		CarModelEntity carModelEntity=carModelRepository.findById(id).get();
		return carModelEntity;
		
	}
	@Override
	public CarModelDto getRoleById(String id) {
		// TODO Auto-generated method stub
		
		CarModelEntity carModelEntity=carModelRepository.getById(id);
		return CarModelMapper.toCarModelDto(carModelEntity);
	}
	
	
}

