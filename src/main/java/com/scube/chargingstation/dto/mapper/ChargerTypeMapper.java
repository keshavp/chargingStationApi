   package com.scube.chargingstation.dto.mapper;

   import java.util.HashSet;
 import java.util.Set;

import com.scube.chargingstation.dto.ChargerTypeDto;
import com.scube.chargingstation.entity.ChargerTypeEntity;

public class ChargerTypeMapper {

	public static ChargerTypeDto toChargerTypeDto(ChargerTypeEntity ChargerTypeEntity) {
 		
        return new ChargerTypeDto()
        		.setName(ChargerTypeEntity.getName())
                .setId(ChargerTypeEntity.getId())
                .setImagePath(ChargerTypeEntity.getImagePath());
	}
	
	public static Set<ChargerTypeDto> toChargerTypeDto(Set<ChargerTypeEntity> ChargerTypeEntities) {
 		
		Set<ChargerTypeDto> ChargerTypeDtos = new HashSet<ChargerTypeDto>();
		for(ChargerTypeEntity ChargerTypeEntity : ChargerTypeEntities) {
			ChargerTypeDtos.add(toChargerTypeDto(ChargerTypeEntity)); 
		}
        return ChargerTypeDtos;
	}

}
