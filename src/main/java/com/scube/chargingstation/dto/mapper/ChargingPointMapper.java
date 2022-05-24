package com.scube.chargingstation.dto.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.scube.chargingstation.dto.AmenityDto;
import com.scube.chargingstation.dto.ChargingPointConnectorDto;
import com.scube.chargingstation.dto.ChargingPointDto;
import com.scube.chargingstation.entity.ChargingPointEntity;

public class ChargingPointMapper {

	public static ChargingPointDto toChargingPointDto(ChargingPointEntity chargingPointEntity) {
		// TODO Auto-generated method stub
		return new ChargingPointDto()
				.setId(chargingPointEntity.getId())
				.setChargingPointId(chargingPointEntity.getChargingPointId())
				.setName(chargingPointEntity.getName())
				.setPartnerName(chargingPointEntity.getPartnerName())
				.setStartTime(chargingPointEntity.getStartTime())
				.setEndTime(chargingPointEntity.getEndTime())
				.setStatus(chargingPointEntity.getStatus())
				
				.setAddress(chargingPointEntity.getAddress())
				.setAddress2(chargingPointEntity.getAddress2())
				.setPincode(chargingPointEntity.getPincode())
				.setLatitude(chargingPointEntity.getLatitude())
				.setLongitude(chargingPointEntity.getLongitude())
				
				.setDistance(chargingPointEntity.getDistance())				
				.setRating(chargingPointEntity.getRating())
				
				.setManufractures(chargingPointEntity.getManufractures())
				.setCommunicationtype(chargingPointEntity.getCommunicationtype())
				.setPowerstandards(chargingPointEntity.getPowerstandards())
				.setStationtype(chargingPointEntity.getStationtype())
				
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