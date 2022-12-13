package com.scube.chargingstation.dto.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scube.chargingstation.dto.ChargingHistoryDto;
import com.scube.chargingstation.dto.ChargingHistoryRespDto;
import com.scube.chargingstation.util.StaticPathContUtils;
import com.scube.chargingstation.util.StringNullEmpty;
import com.scube.chargingstation.util.UploadPathContUtils;

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
			obj.setRemark(StringNullEmpty.stringNullAndEmptyToBlank(pojo.getRemark()));
			obj.setChargingReqId(pojo.getChargingRequestId());
			
			if(pojo.getChargingRequestId()!=null&&!pojo.getChargingRequestId().isEmpty())
			{
				obj.setReceipt(StaticPathContUtils.APP_URL_DIR+StaticPathContUtils.SET_RECEIPT_FILE_URL_DIR +pojo.getChargingRequestId());
			}else {
				if(pojo.getBookingRequestId()!=null && !pojo.getBookingRequestId().isEmpty()) {
					if(pojo.getRemark().contains("No Refund")) {
						obj.setReceipt(StaticPathContUtils.APP_URL_DIR+StaticPathContUtils.SET_BOOKING_RECEIPT_FILE_URL_DIR +pojo.getBookingRequestId());
					}
				}
			}
			resp.add(obj);
		}
		return resp;
	}
}
