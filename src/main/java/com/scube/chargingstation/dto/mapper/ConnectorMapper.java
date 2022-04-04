package com.scube.chargingstation.dto.mapper;

import java.util.HashSet;
import java.util.Set;

import com.scube.chargingstation.dto.ConnectorDto;
import com.scube.chargingstation.entity.ConnectorEntity;

public class ConnectorMapper {

	public static ConnectorDto toConnectorDto(ConnectorEntity connectorEntity) {
 		
        return new ConnectorDto()
        		//.setId(userInfo.getId())
        		.setConnectorId(connectorEntity.getConnectorId())
        		.setChargingPointEntity(connectorEntity.getChargingPointEntity().getChargingPointId());
	}
	
	public static Set<ConnectorDto> toConnectorsDto(Set<ConnectorEntity> connectorEntities) {
 		
		Set<ConnectorDto> connectorDtos = new HashSet<ConnectorDto>();
		for(ConnectorEntity connectorEntity : connectorEntities) {
			connectorDtos.add(toConnectorDto(connectorEntity)); 
		}
        return connectorDtos;
	}

}
