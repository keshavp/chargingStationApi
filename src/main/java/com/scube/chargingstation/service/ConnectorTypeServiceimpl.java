  package com.scube.chargingstation.service;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.net.MediaType;
import com.scube.chargingstation.dto.ChargerTypeDto;
import com.scube.chargingstation.dto.ChargingPointConnectorDto;
import com.scube.chargingstation.dto.ConnectorTypeDto;
import com.scube.chargingstation.dto.incoming.ConnectorTypeIncomingDto;
import com.scube.chargingstation.dto.mapper.ConnectorTypeMapper;
import com.scube.chargingstation.dto.response.Response;
import com.scube.chargingstation.entity.ChargerTypeEntity;
import com.scube.chargingstation.exception.BRSException;
import com.scube.chargingstation.exception.EntityType;
import com.scube.chargingstation.repository.ConnectorTypeRepository;
import com.scube.chargingstation.util.FileStorageService;

import static com.scube.chargingstation.exception.ExceptionType.ALREADY_EXIST;
@Service
public class ConnectorTypeServiceimpl implements ConnectorTypeService {

	private static final Logger logger = LoggerFactory.getLogger(ConnectorTypeServiceimpl.class);
	@Autowired
	ConnectorTypeRepository connectorTypeRepository;
	
	ConnectorTypeService connectorTypeService;
	 @Autowired
	 private FileStorageService fileStorageService;

	@Override
	public boolean addConnectorType( ConnectorTypeIncomingDto connectorTypeIncomingDto) {
		// TODO Auto-generated method stub
		
		logger.info("********ConnectorServiceImpl addConnectorType********");
		if(connectorTypeIncomingDto.getName()==" ")
		{
			throw BRSException.throwException("Connector name can't be blank");
		}
		
		if(connectorTypeIncomingDto.getImagePath()==" ")
		{
			throw BRSException.throwException("Connector image can't be blank");	
		}
		
		if(connectorTypeIncomingDto.getStatus()==" ")
		{
			throw BRSException.throwException("Connector status can't be blank");
		}
		
		ChargerTypeEntity chargerCodeDuplicateCheck=connectorTypeRepository.findByName(connectorTypeIncomingDto.getName());
		if(chargerCodeDuplicateCheck!= null) {
		
			logger.error("throw error that user already exists for Name = "+ connectorTypeIncomingDto.getName());
			throw BRSException.throwException(EntityType.ChargerType, ALREADY_EXIST, connectorTypeIncomingDto.getName());
		}
		
		ChargerTypeEntity chargerTypeEntity=new ChargerTypeEntity();
		chargerTypeEntity.setName(connectorTypeIncomingDto.getName());
		chargerTypeEntity.setImagePath(connectorTypeIncomingDto.getImagePath());
		chargerTypeEntity.setStatus(connectorTypeIncomingDto.getStatus());
		chargerTypeEntity.setIsdeleted("N");
		connectorTypeRepository.save(chargerTypeEntity);
		
		
		return true;
	}

	@Override
	public boolean editConnectorType(@Valid ConnectorTypeIncomingDto connectorTypeIncomingDto) {
		// TODO Auto-generated method stub
		logger.info("********ConnectorServiceImpl addConnectorType********");
		if(connectorTypeIncomingDto.getName()==" " || connectorTypeIncomingDto.getName()==null )
		{
			throw BRSException.throwException("Connector name can't be blank");
		}
		
		if(connectorTypeIncomingDto.getImagePath()==" ")
		{
			throw BRSException.throwException("Connector image can't be blank");	
		}
		
		if(connectorTypeIncomingDto.getStatus()==" " || connectorTypeIncomingDto.getStatus()==null)
		{
			throw BRSException.throwException("Connector status can't be blank");
		}
		
	   	ChargerTypeEntity chargerTypeEntity=connectorTypeRepository.findById(connectorTypeIncomingDto.getId()).get();
		ChargerTypeEntity chargerCodeDuplicateCheck=connectorTypeRepository.findByNameAndIdNot(connectorTypeIncomingDto.getName(), connectorTypeIncomingDto.getId());
		if(chargerCodeDuplicateCheck!= null) {
		
			logger.error("throw error that user already exists for Name = "+ connectorTypeIncomingDto.getName());
			throw BRSException.throwException(EntityType.ChargerType, ALREADY_EXIST, connectorTypeIncomingDto.getName());
		}
		chargerTypeEntity.setName(connectorTypeIncomingDto.getName());
		chargerTypeEntity.setImagePath(connectorTypeIncomingDto.getImagePath());
		chargerTypeEntity.setStatus(connectorTypeIncomingDto.getStatus());
		connectorTypeRepository.save(chargerTypeEntity);	
		
		return true;
	}
	@Override
	public boolean deleteConnectorType(String id) {
		// TODO Auto-generated method stub
		ChargerTypeEntity chargerTypeEntity=connectorTypeRepository.findById(id).get();
		
        if(chargerTypeEntity.getId()==" " || chargerTypeEntity.getId()==null) {
        	throw BRSException.throwException("Connector id can't be blank or null");
        	
        }
        chargerTypeEntity.setStatus("INACTIVE");
        chargerTypeEntity.setIsdeleted("Y");
        connectorTypeRepository.save(chargerTypeEntity);	
		return true;
	}

	
	
	@Override
	public ConnectorTypeDto getConnectorTypeById(String id) {
		// TODO Auto-generated method stub
		ChargerTypeEntity chargerTypeEntity=connectorTypeRepository.getById(id);
		
		return ConnectorTypeMapper.toConnectorTypeDto(chargerTypeEntity);
	}

	@Override
	public List<ConnectorTypeDto> getConnectors() {
		// TODO Auto-generated method stub
		
		List<ChargerTypeEntity> chargerTypeEntities=connectorTypeRepository.findAll();

		
		return ConnectorTypeMapper.connectorTypeDtos(chargerTypeEntities);
	}

	@Override
	public ChargerTypeEntity getChargerTypeEntityByName(String name) {
		// TODO Auto-generated method stub
		return connectorTypeRepository.findByName(name);
	}
	@Override
	public String saveDocument(MultipartFile file) {
		// TODO Auto-generated method stub	
		String fileSubPath = "CT";
		String filePath;
		filePath = fileStorageService.storeFile(file , fileSubPath);
		logger.info("---------ConnecterTypeServiceImpl saveDocument----------------");
	
	
		return filePath;
	}

}
