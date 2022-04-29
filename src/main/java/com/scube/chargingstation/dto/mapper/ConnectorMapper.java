package com.scube.chargingstation.dto.mapper;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;

import com.scube.chargingstation.dto.ConnectorDto;
import com.scube.chargingstation.entity.ConnectorEntity;
import org.springframework.beans.factory.annotation.Value;


public class ConnectorMapper {
	
	/*
	 * @Value("${chargingstation.chargertype}") static String imgLocation;
	 */	
	public static ConnectorDto toConnectorDto(ConnectorEntity connectorEntity) {
		
        return new ConnectorDto()
        		//.setId(userInfo.getId())
        		.setConnectorId(connectorEntity.getConnectorId())
        		//.setChargingPoint(connectorEntity.getChargingPointEntity().getChargingPointId())
        		.setChargerId(connectorEntity.getChargerTypeEntity().getId())
        		.setChargerType(connectorEntity.getChargerTypeEntity().getName())
        
        		.setImage("http://125.99.153.126:8085/chargingStationApi/api/v1/images/getImage/CT/"+connectorEntity.getChargerTypeEntity().getId());
	}
	
	public static Set<ConnectorDto> toConnectorsDto(Set<ConnectorEntity> connectorEntities) {
 		
		Set<ConnectorDto> connectorDtos = new HashSet<ConnectorDto>();
		for(ConnectorEntity connectorEntity : connectorEntities) {
			connectorDtos.add(toConnectorDto(connectorEntity)); 
		}
        return connectorDtos;
	}

}
