package com.scube.chargingstation.dto.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scube.chargingstation.dto.AmenityDto;
import com.scube.chargingstation.dto.ChargingHistoryDto;
import com.scube.chargingstation.dto.ChargingHistoryRespDto;
import com.scube.chargingstation.dto.ChargingPointConnectorDto;
import com.scube.chargingstation.dto.ChargingPointDto;
import com.scube.chargingstation.entity.ChargingPointEntity;
import com.scube.chargingstation.util.StaticPathContUtils;

public class ChargingPointMapper {

	public static ChargingPointDto toChargingPointDto(ChargingPointEntity chargingPointEntity) {
		// TODO Auto-generated method stub
		return new ChargingPointDto()
				.setId(chargingPointEntity.getId())
				.setChargingPointId(chargingPointEntity.getChargingPointId())
				.setName(chargingPointEntity.getName())
				.setPartnerName(chargingPointEntity.getPartner().getPartnerName())
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
				.setPartner(chargingPointEntity.getPartner().getId())
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
	
	
	public static List<ChargingPointDto> toChargingPointDtoLst(List<Map<String, String>>  list) {
		// TODO Auto-generated method stub
		final ObjectMapper mapper = new ObjectMapper(); 
		List<ChargingPointDto> resp = new ArrayList<>();
		
		for (int i = 0; i < list.size(); i++) 
		{
			ChargingPointDto obj=new ChargingPointDto();
			
			final ChargingPointDto pojo = mapper.convertValue(list.get(i), ChargingPointDto.class);
			
			obj.setId(pojo.getId());
			obj.setChargingPointId(pojo.getChargingPointId());
			obj.setName(pojo.getName());
			obj.setPartnerName(pojo.getPartnerName());
			obj.setStartTime(pojo.getStartTime());
			obj.setEndTime(pojo.getEndTime());
			obj.setStatus(pojo.getStatus());
			
			
			obj.setAddress(pojo.getAddress());
			obj.setAddress2(pojo.getAddress2());
			obj.setPincode(pojo.getPincode());
			obj.setLatitude(pojo.getLatitude());
			obj.setLongitude(pojo.getLongitude());
			
			obj.setDistance(pojo.getDistance())	;			
			obj.setRating(pojo.getRating());
			
			obj.setManufractures(pojo.getManufractures());
			obj.setCommunicationtype(pojo.getCommunicationtype());
			obj.setPowerstandards(pojo.getPowerstandards());
			obj.setStationtype(pojo.getStationtype());
			obj.setPartner(pojo.getPartner());////////
			obj.setAmenities(pojo.getAmenities());
			obj.setConnectors(pojo.getConnectors());
			obj.setFardistance(pojo.getFardistance());
			
			resp.add(obj);
		}
		return resp;
	}
	
}