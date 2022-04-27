package com.scube.chargingstation.dto.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scube.chargingstation.dto.ChargingHistoryDto;
import com.scube.chargingstation.dto.ChargingHistoryRespDto;

public class ChargingHistoryMapper {

	
	
	public static List<ChargingHistoryRespDto> toChargingHistoryDto(List<Map<String, String>>  list) {
		// TODO Auto-generated method stub
		final ObjectMapper mapper = new ObjectMapper(); 
		List<ChargingHistoryRespDto> resp = new ArrayList<>();
		
		for (int i = 0; i < list.size(); i++) 
		{
			ChargingHistoryRespDto obj=new ChargingHistoryRespDto();
			
			final ChargingHistoryDto pojo = mapper.convertValue(list.get(i), ChargingHistoryDto.class);
			obj.setTransactionDate(pojo.getTransactionDate());
			obj.setAmount(pojo.getAmount());
			obj.setRemark(pojo.getRemark());
			
			if(pojo.getChargingRequestId()!=null&&!pojo.getChargingRequestId().isEmpty())
			{
			obj.setReceipt("http://125.99.153.126:8085/chargingStationApi/api/v1/images/getChargingRequestReceipt/"+pojo.getChargingRequestId());
			}
			
			resp.add(obj);
		}
		return resp;
	}
}
