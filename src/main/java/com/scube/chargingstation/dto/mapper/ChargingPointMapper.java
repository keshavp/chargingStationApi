package com.scube.chargingstation.dto.mapper;

import com.scube.chargingstation.dto.ChargingPointDto;
import com.scube.chargingstation.entity.ChargingPointEntity;

public class ChargingPointMapper {

	public static ChargingPointDto toChargingPointDto(ChargingPointEntity chargingPointEntity) {
		// TODO Auto-generated method stub
		return new ChargingPointDto()
				.setChargingPointId(chargingPointEntity.getChargingPointId())
				.setConnectorDto(ConnectorMapper.toConnectorsDto(chargingPointEntity.getConnectorEntities()));
	}

}
