package com.scube.chargingstation.dto.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scube.chargingstation.dto.ChargingHistoryDto;

public class ChargingHistoryMapper {

	
	
	public static List<ChargingHistoryDto> toChargingHistoryDto(List<Map<String, String>>  list) {
		// TODO Auto-generated method stub
		final ObjectMapper mapper = new ObjectMapper(); 
		List<ChargingHistoryDto> resp = new ArrayList<>();
		
		for (int i = 0; i < list.size(); i++) 
		{
			final ChargingHistoryDto pojo = mapper.convertValue(list.get(i), ChargingHistoryDto.class);
			resp.add(pojo);
		}
		return resp;
	}
}
