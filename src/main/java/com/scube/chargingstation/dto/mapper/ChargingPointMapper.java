package com.scube.chargingstation.dto.mapper;

import java.util.ArrayList;
import java.util.List;

import com.scube.chargingstation.dto.ChargingPointDto;
import com.scube.chargingstation.entity.ChargingPointEntity;

public class ChargingPointMapper {

	public static ChargingPointDto toChargingPointDto(ChargingPointEntity chargingPointEntity) {
		// TODO Auto-generated method stub
		return new ChargingPointDto()
				.setChargingPointId(chargingPointEntity.getChargingPointId())
				.setAddress(chargingPointEntity.getAddress())
				.setDistance(chargingPointEntity.getDistance())
				.setLatitude(chargingPointEntity.getLatitude())
				.setLogitude(chargingPointEntity.getLogitude())
				.setName(chargingPointEntity.getName())
				.setRating(chargingPointEntity.getStatus())
				.setStatus(chargingPointEntity.getStatus())
				.setAmenities(AmenityMapper.toAmenitiesDto(chargingPointEntity.getAmenities()))
				.setConnectors(ConnectorMapper.toConnectorsDto(chargingPointEntity.getConnectorEntities()));
								
	}

	public static List<ChargingPointDto> toChargingPointDto(List<ChargingPointEntity> chargingPointEntities) {
		// TODO Auto-generated method stub
		
		List<ChargingPointDto>	chargingPointDtos	= new ArrayList<ChargingPointDto>();
		
		for(ChargingPointEntity chargingPointEntity : chargingPointEntities) {
			chargingPointDtos.add(toChargingPointDto(chargingPointEntity));
		}
		
		return chargingPointDtos;
	}
	
}
