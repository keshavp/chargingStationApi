  package com.scube.chargingstation.dto.mapper;

  import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.scube.chargingstation.dto.CarModelDto;
import com.scube.chargingstation.entity.CarModelEntity;

public class CarModelMapper {

	public static CarModelDto toCarModelDto(CarModelEntity carModelEntity) {
 		
      return new CarModelDto()
    		  .setId(carModelEntity.getId())
        		.setModel(carModelEntity.getModel())
        		.setDescription(carModelEntity.getDescription())
        		.setChargertypes(ChargerTypeMapper.toChargerTypeDto(carModelEntity.getChargertypes()));
               
	}
	
	public static List<CarModelDto> toCarModelDto(List<CarModelEntity> CarModelEntities) {
 		
		List<CarModelDto> CarModelDtos = new ArrayList<CarModelDto>();
		
		for(CarModelEntity CarModelEntity : CarModelEntities) {
			CarModelDtos.add(toCarModelDto(CarModelEntity)); 
		}
        return CarModelDtos;
	}

}
