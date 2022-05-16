package com.scube.chargingstation.dto.mapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.scube.chargingstation.dto.AmenityDto;
import com.scube.chargingstation.entity.AmenitiesEntity;

public class AmenityMapper {

	public static AmenityDto toAmenityDto(AmenitiesEntity amenityEntity) {
 		
        return new AmenityDto()
        		.setName(amenityEntity.getName())
        		.setId(amenityEntity.getId())
        		.setStatus(amenityEntity.getStatus());
	}
	
	public static Set<AmenityDto> toAmenitiesDto(Set<AmenitiesEntity> amenityEntities) {
 		
		Set<AmenityDto> amenityDtos = new HashSet<AmenityDto>();
		for(AmenitiesEntity amenityEntity : amenityEntities) {
			amenityDtos.add(toAmenityDto(amenityEntity)); 
		}
        return amenityDtos;
	}
	
	public static List<AmenityDto> toAmenitiesDto(List<AmenitiesEntity> amenityEntities) {
 		
		List<AmenityDto> amenityDtos = new ArrayList<AmenityDto>();
		for(AmenitiesEntity amenityEntity : amenityEntities) {
			amenityDtos.add(toAmenityDto(amenityEntity)); 
		}
        return amenityDtos;
	}

}
