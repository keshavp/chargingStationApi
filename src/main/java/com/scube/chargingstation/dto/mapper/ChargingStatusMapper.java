package com.scube.chargingstation.dto.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scube.chargingstation.dto.ChargingStatusRespDto;

public class ChargingStatusMapper {
	
	public static List<ChargingStatusRespDto> toChargingStatusRespDto(List<Map<String, String>>  list) {
		// TODO Auto-generated method stub
		final ObjectMapper mapper = new ObjectMapper(); 
		List<ChargingStatusRespDto> resp = new ArrayList<>();
		
		for (int i = 0; i < list.size(); i++) 
		{
			ChargingStatusRespDto obj=new ChargingStatusRespDto();
			
			final ChargingStatusRespDto pojo = mapper.convertValue(list.get(i), ChargingStatusRespDto.class);
		
			obj.setId(pojo.getId());
			obj.setChargingPercent(pojo.getChargingPercent());
			obj.setChargingSpeed(pojo.getChargingSpeed());
			obj.setEstimatedTime(pojo.getEstimatedTime());
			obj.setRequestedAmount(pojo.getRequestedAmount());
			obj.setRequestedKwh(pojo.getRequestedKwh());
			obj.setCurrentKwh(pojo.getCurrentKwh());
			obj.setVehicleNo(pojo.getVehicleNo());
			resp.add(obj);
		}
		return resp;
								
	}
}
