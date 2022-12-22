package com.scube.chargingstation.dto.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scube.chargingstation.dto.ChargingHistoryDto;
import com.scube.chargingstation.dto.ChargingHistoryRespDto;
import com.scube.chargingstation.service.ChargingRequestServiceImpl;
import com.scube.chargingstation.util.StaticPathContUtils;
import com.scube.chargingstation.util.StringNullEmpty;
import com.scube.chargingstation.util.UploadPathContUtils;

public class ChargingHistoryMapper {
	
	private static final Logger logger = LoggerFactory.getLogger(ChargingHistoryMapper.class);


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
	
	public static List<ChargingHistoryRespDto> toChargingHistoryDtoNew(List<Map<String, String>>  list) {
		// TODO Auto-generated method stub
		final ObjectMapper mapper = new ObjectMapper(); 
		List<ChargingHistoryRespDto> resp = new ArrayList<>();
		
		for (int i = 0; i < list.size(); i++) 
		{
			ChargingHistoryRespDto obj=new ChargingHistoryRespDto();
			
			final ChargingHistoryDto pojo = mapper.convertValue(list.get(i), ChargingHistoryDto.class);
			
			
		//	System.out.println("tid"+pojo.getTransactionId());
			//System.out.println("o id"+pojo.getOrderId());
			
			if((pojo.getTransactionType().equals("Credit"))&&(pojo.getTransactionId()==null)&&(pojo.getOrderId()!=null))
			{
				//failed transactions
				
				logger.info("failed tr details"+pojo.getTransactionType()+"tid"+pojo.getTransactionId()+"o id"+pojo.getOrderId());
				logger.info("for failed"+pojo.getTransactionDate());

				
			}
			else {     
				
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
			
			
			
		}
		return resp;
	}
	
}
