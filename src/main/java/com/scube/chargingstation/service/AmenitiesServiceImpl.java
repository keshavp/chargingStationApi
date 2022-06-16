package com.scube.chargingstation.service;

import static com.scube.chargingstation.exception.ExceptionType.ALREADY_EXIST;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scube.chargingstation.dto.AmenityDto;
import com.scube.chargingstation.dto.incoming.AmenitiesIncomingDto;
import com.scube.chargingstation.dto.mapper.AmenityMapper;
import com.scube.chargingstation.entity.AmenitiesEntity;
import com.scube.chargingstation.exception.BRSException;
import com.scube.chargingstation.exception.EntityType;
import com.scube.chargingstation.repository.AmenitiesRepository;

import ch.qos.logback.classic.Logger;


@Service
public class AmenitiesServiceImpl implements AmenitiesService{
	
	private static final Logger logger = (Logger) LoggerFactory.getLogger(AmenitiesServiceImpl.class);
	
	@Autowired
	AmenitiesRepository amenitiesRepository;
	
	@Override
	public boolean addAmenities(@Valid AmenitiesIncomingDto amenitiesIncomingDto) {
		// TODO Auto-generated method stub
	//	logger.info("********AmenitiesServiceImpl addAmenities********"+amenitiesIncomingDto.getName()+"--"+amenitiesIncomingDto.getName().trim().isEmpty());
		
		if((amenitiesIncomingDto.getName()=="") || (amenitiesIncomingDto.getName().trim().isEmpty())) {
			throw BRSException.throwException("Error : Amenity name can't be blank");
		}
		
		/*
		 * if((amenitiesIncomingDto.getStatus()=="") ||
		 * (amenitiesIncomingDto.getStatus().trim().isEmpty())) { throw
		 * BRSException.throwException("Error : Status can't be blank"); }
		 */
		
		
		logger.info(amenitiesIncomingDto.getName());
		
		AmenitiesEntity checkNameEntity = amenitiesRepository.findByName(amenitiesIncomingDto.getName());
		
		
		
		if(checkNameEntity != null) {
			
			logger.error("Throws an error that Error : Amenity Name already exists = "+ amenitiesIncomingDto.getName());
			throw BRSException.throwException(EntityType.AMENITY, ALREADY_EXIST, amenitiesIncomingDto.getName());
		}
	
		AmenitiesEntity amenitiesEntity = new AmenitiesEntity();
		amenitiesEntity.setName(amenitiesIncomingDto.getName());
		amenitiesEntity.setStatus(amenitiesIncomingDto.getStatus());
		amenitiesEntity.setIsdeleted("N");
		amenitiesRepository.save(amenitiesEntity);
		
		return true;	
		
	}
	
	@Override
	public boolean editAmenities(@Valid AmenitiesIncomingDto amenitiesIncomingDto) {
		
		logger.info("********AmenitiesServiceImpl editAmenities********");
	
		
		if((amenitiesIncomingDto.getName()=="") || (amenitiesIncomingDto.getName().trim().isEmpty())) {
			throw BRSException.throwException("Error : Amenity name can't be blank");
		}
		
		
		if((amenitiesIncomingDto.getId()=="") || (amenitiesIncomingDto.getId().trim().isEmpty())) {
			throw BRSException.throwException("Error : Amenity ID can't be blank");
		}	
	
		
		/*
		 * if((amenitiesIncomingDto.getStatus()=="") ||
		 * (amenitiesIncomingDto.getStatus().trim().isEmpty())) { throw
		 * BRSException.throwException("Error : Status can't be blank"); }
		 */
		/*
		AmenitiesEntity checkIdEntity = amenitiesRepository.findById(amenitiesIncomingDto.getId()).get();
		if(checkIdEntity!=null) {
			// logger.error("Throws an error that Amenity Name already exists = "+ amenitiesIncomingDto.getId());
			throw BRSException.throwException("Throws an error that Amenity ID already exists = " + amenitiesIncomingDto.getId());
		}
		*/
		
		AmenitiesEntity amenitiesEntity = amenitiesRepository.findById(amenitiesIncomingDto.getId()).get();
		
		AmenitiesEntity amenitiesCodeDuplicateCheck = amenitiesRepository.findByNameAndIdNot(amenitiesIncomingDto.getName(), amenitiesIncomingDto.getId());
		if(amenitiesCodeDuplicateCheck != null) {
		
			logger.error("throw error that Error : Amenity Name already exists = " + amenitiesIncomingDto.getName());
			throw BRSException.throwException(EntityType.AMENITY, ALREADY_EXIST, amenitiesIncomingDto.getName());
		}
		
		
		
	//	AmenitiesEntity amenitiesEntity = amenitiesRepository.findById(amenitiesIncomingDto.getId()).get();
	//	AmenitiesEntity amenitiesEntity = new AmenitiesEntity();
		amenitiesEntity.setName(amenitiesIncomingDto.getName());
//		amenitiesEntity.setStatus(amenitiesIncomingDto.getStatus());
		// amenitiesEntity.setId(amenitiesIncomingDto.getId());
		amenitiesEntity.setIsdeleted("N");
		
		amenitiesRepository.save(amenitiesEntity);
		
		return true;
	}
	
	/*
	@Override
	public boolean deleteAmenities(@Valid AmenitiesIncomingDto amenitiesIncomingDto) {
		// TODO Auto-generated method stub
		AmenitiesEntity amenitiesEntity = amenitiesRepository.findById(amenitiesIncomingDto.getId()).get();
		
		if(amenitiesEntity.getId() == "" || amenitiesEntity.getId().trim().isEmpty()) {
			
			throw BRSException.throwException("Error : Amenity ID can't be blank");
		}
	
		
		amenitiesEntity.setIsdeleted("Y");
		amenitiesEntity.setStatus("INACTIVE");
		amenitiesRepository.save(amenitiesEntity);
		
		return true;
	}
	*/  

	
	
	@Override
	public boolean deleteAmenities(String id) {
		// TODO Auto-generated method stub
		AmenitiesEntity amenitiesEntity = amenitiesRepository.findById(id).get();
		
		if(amenitiesEntity.getId() == "" || amenitiesEntity.getId().trim().isEmpty()) {
			
			throw BRSException.throwException("Error : Amenity ID can't be blank or null");
		}
	
		
//		amenitiesEntity.setIsdeleted("Y");
//		amenitiesEntity.setStatus("Inactive");
		amenitiesRepository.delete(amenitiesEntity);
		
		return true;
	}
	
	
	@Override
	public  List<AmenityDto> getAllAmenities() {
		// TODO Auto-generated method stub
		
	   //  List<AmenitiesEntity> amenitiesEntities = amenitiesRepository.findAll();
		List<AmenitiesEntity> amenitiesEntities = amenitiesRepository.findAll();
		/*
		if (amenitiesEntities.status == null || amenitiesEntities.status.isEmpty() || amenitiesEntities.status.trim().isEmpty()) {
			return "";
		}
		*/
		return  AmenityMapper.toAmenitiesDto(amenitiesEntities);
	}
	
	/*
	@Override
	public AmenitiesEntity findByName(String name) {
		// TODO Auto-generated method stub
		AmenitiesEntity amenitiesEntity = amenitiesRepository.findByName(name);
		
		return amenitiesEntity;	
		
	}
	*/
	
	@Override
	public AmenityDto getAmenitiesById(String id) {
		// TODO Auto-generated method stub
		
		Optional<AmenitiesEntity> amenitiesEntities = amenitiesRepository.findById(id);
		AmenitiesEntity AmenitiesEnt=new AmenitiesEntity ();
		
		if(amenitiesEntities!=null)
		 AmenitiesEnt=amenitiesEntities.get();
		
		
		return AmenityMapper.toAmenityDto(AmenitiesEnt);

	}
	
	/*
	@Override
	public AmenitiesEntity findById(String id) {
		// TODO Auto-generated method stub
		AmenitiesEntity amenitiesEntities = amenitiesRepository.findById(id).get();
		
		return amenitiesEntities;	
	}
	*/

	@Override
	public AmenitiesEntity findAmenitiesEntityByName(String name) {
		// TODO Auto-generated method stub
		
		return amenitiesRepository.findByName(name);
	}

	@Override
	public List<AmenityDto> getActiveAmenities() {
		// TODO Auto-generated method stub
	   //  List<AmenitiesEntity> amenitiesEntities = amenitiesRepository.findAll();
		List<AmenitiesEntity> amenitiesEntities = amenitiesRepository.findByStatus("Active");
		return  AmenityMapper.toAmenitiesDto(amenitiesEntities);
	}
}

