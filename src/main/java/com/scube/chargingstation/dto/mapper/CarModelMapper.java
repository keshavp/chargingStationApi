  package com.scube.chargingstation.dto.mapper;

  import java.util.ArrayList;
import java.util.List;

import com.scube.chargingstation.dto.CarModelDto;
import com.scube.chargingstation.entity.CarModelEntity;
import com.scube.chargingstation.util.StaticPathContUtils;

public class CarModelMapper {

	public static CarModelDto toCarModelDto(CarModelEntity carModelEntity) {
 		
      return new CarModelDto()
    		  .setId(carModelEntity.getId())
        	  .setModel(carModelEntity.getModel())
        	  .setDescription(carModelEntity.getDescription())
        	  .setChargertypes(ChargerTypeMapper.toChargerTypeDto(carModelEntity.getChargertypes()))
        	  .setImagePath(StaticPathContUtils.APP_URL_DIR+StaticPathContUtils.SET_CAR_MODEL_TYPE_FILE_URL_DIR+carModelEntity.getId())
        	  .setFilename(carModelEntity.getImagePath())
              .setStatus(carModelEntity.getStatus());
               
	}
	
	public static List<CarModelDto> toCarModelDto(List<CarModelEntity> CarModelEntities) {
 		
		List<CarModelDto> CarModelDtos = new ArrayList<CarModelDto>();
		
		for(CarModelEntity CarModelEntity : CarModelEntities) {
			CarModelDtos.add(toCarModelDto(CarModelEntity)); 
		}
        return CarModelDtos;
	}

}
