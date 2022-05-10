package com.scube.chargingstation.service;

import java.util.List;

import javax.validation.Valid;

import com.scube.chargingstation.dto.AmenityDto;
import com.scube.chargingstation.dto.incoming.AmenitiesIncomingDto;
import com.scube.chargingstation.entity.AmenitiesEntity;

public interface AmenitiesService {
	
	// AmenitiesEntity findByName(String name);
	// AmenitiesEntity findById(String id);
	
	boolean addAmenities (@Valid AmenitiesIncomingDto amenitiesIncomingDto);
	boolean editAmenities(@Valid AmenitiesIncomingDto amenitiesIncomingDto);
	boolean deleteAmenities(@Valid AmenitiesIncomingDto amenitiesIncomingDto);
	
	// boolean deleteAmenities(String id);
	
	List<AmenityDto>  getAllAmenities();
	// List<AmenityDto> findById();
	
	AmenityDto getAmenitiesById(String id);
	
}
