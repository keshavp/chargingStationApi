package com.scube.chargingstation.dto.mapper;

import java.util.HashSet;
import java.util.Set;

import com.scube.chargingstation.dto.AmenityDto;
import com.scube.chargingstation.entity.AmenitiesEntity;

public class AmenityMapper {

	public static AmenityDto toAmenityDto(AmenitiesEntity amenityEntity) {
 		
        return new AmenityDto()
        		.setName(amenityEntity.getName());
	}
	
	public static Set<AmenityDto> toAmenitiesDto(Set<AmenitiesEntity> amenityEntities) {
 		
		Set<AmenityDto> amenityDtos = new HashSet<AmenityDto>();
		for(AmenitiesEntity amenityEntity : amenityEntities) {
			amenityDtos.add(toAmenityDto(amenityEntity)); 
		}
        return amenityDtos;
	}

}
