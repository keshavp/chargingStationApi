package com.scube.chargingstation.dto.mapper;

import java.util.List;

import com.scube.chargingstation.dto.AverageSessionDto;
import com.scube.chargingstation.entity.AmenitiesEntity;

public class AverageSessionMapper {

	/*
	 * public static AverageSessionDto toAverageSessionDto(AmenitiesEntity
	 * AverageSessionEntity) {
	 * 
	 * return new AverageSessionDto() .setAveragehour(null) .setAverageType(null); }
	 */

	public static AverageSessionDto toAverageSessionDto(String averageType, String hour) {
		// TODO Auto-generated method stub
		 return new AverageSessionDto()
	        		.setAveragehour(hour)
	        		.setAverageType(averageType);
	}
}
