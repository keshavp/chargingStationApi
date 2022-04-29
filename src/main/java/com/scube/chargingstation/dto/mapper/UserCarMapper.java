package com.scube.chargingstation.dto.mapper;

import java.util.ArrayList;
import java.util.List;

import com.scube.chargingstation.dto.UserCarRespDto;
import com.scube.chargingstation.entity.UserCarsEntity;  

public class UserCarMapper {

	public static UserCarRespDto toUserCarDto(UserCarsEntity carModelEntity) {
 		
      return new UserCarRespDto()
    		  .setId(carModelEntity.getId())
        		.setModel(carModelEntity.getCarModelEntity().getModel())
        		.setVehicleNo(carModelEntity.getVehicleNo());
        		//.setDescription(carModelEntity.getDescription())
        		//.setChargertypes(ChargerTypeMapper.toChargerTypeDto(carModelEntity.getChargertypes())
    		 // );
	}
	
	public static List<UserCarRespDto> toUserCarDto(List<UserCarsEntity> userCarEntities) {
 		
		List<UserCarRespDto> UserCarDtos = new ArrayList<UserCarRespDto>();
		
		for(UserCarsEntity userCarEntity : userCarEntities) {
			UserCarDtos.add(toUserCarDto(userCarEntity)); 
		}
        return UserCarDtos;
	}

}
