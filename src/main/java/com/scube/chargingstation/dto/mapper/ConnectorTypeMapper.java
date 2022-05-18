package com.scube.chargingstation.dto.mapper;

import java.util.ArrayList;
import java.util.List;

import com.scube.chargingstation.dto.ConnectorTypeDto;
import com.scube.chargingstation.dto.RoleDto;
import com.scube.chargingstation.entity.ChargerTypeEntity;
import com.scube.chargingstation.util.StaticPathContUtils;

public class ConnectorTypeMapper {

	public static ConnectorTypeDto toConnectorTypeDto(ChargerTypeEntity chargerTypeEntity) {
		return new ConnectorTypeDto()
				
				.setName(chargerTypeEntity.getName())
				.setImagePath(StaticPathContUtils.APP_URL_DIR+StaticPathContUtils.SET_CHARGER_TYPE_FILE_URL_DIR+chargerTypeEntity.getId())
				.setStatus(chargerTypeEntity.getStatus())
		         .setId(chargerTypeEntity.getId());
	}
	
public static List<ConnectorTypeDto> connectorTypeDtos(List<ChargerTypeEntity> chargerTypeEntities) {
	// TODO Auto-generated method stub
	List<ConnectorTypeDto> connectorTypeDtos = new ArrayList<ConnectorTypeDto>();
	for(ChargerTypeEntity chargerTypeEntity : chargerTypeEntities) {
		connectorTypeDtos.add((ConnectorTypeDto) toConnectorTypeDto(chargerTypeEntity)); 
	}
	return connectorTypeDtos;


       }
}
