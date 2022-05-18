package com.scube.chargingstation.dto.mapper;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;

import com.scube.chargingstation.dto.ChargingPointConnectorDto;
import com.scube.chargingstation.entity.ConnectorEntity;
import com.scube.chargingstation.util.StaticPathContUtils;

import org.springframework.beans.factory.annotation.Value;


public class ConnectorMapper {
	
	/*
	 * @Value("${chargingstation.chargertype}") static String imgLocation;
	 */	
	public static ChargingPointConnectorDto toConnectorDto(ConnectorEntity connectorEntity) {
		
        return new ChargingPointConnectorDto()
        		//.setId(userInfo.getId())
        		.setConnectorId(connectorEntity.getConnectorId())
        		//.setChargingPoint(connectorEntity.getChargingPointEntity().getChargingPointId())
        		.setChargerId(connectorEntity.getChargerTypeEntity().getId())
        		.setChargerType(connectorEntity.getChargerTypeEntity().getName())
        
        		.setImage(StaticPathContUtils.APP_URL_DIR+StaticPathContUtils.SET_CHARGER_TYPE_FILE_URL_DIR+connectorEntity.getChargerTypeEntity().getId());
	}
	
	public static Set<ChargingPointConnectorDto> toConnectorsDto(Set<ConnectorEntity> connectorEntities) {
 		
		Set<ChargingPointConnectorDto> connectorDtos = new HashSet<ChargingPointConnectorDto>();
		for(ConnectorEntity connectorEntity : connectorEntities) {
			connectorDtos.add(toConnectorDto(connectorEntity)); 
		}
        return connectorDtos;
	}

}
