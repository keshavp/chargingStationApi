package com.scube.chargingstation.dto.mapper;

import java.nio.charset.IllegalCharsetNameException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;

import com.scube.chargingstation.dto.ChargingPointConnectorDto;
import com.scube.chargingstation.dto.ChargingRequestRespDto;
import com.scube.chargingstation.entity.ChargingRequestEntity;
import com.scube.chargingstation.entity.ConnectorEntity;
import com.scube.chargingstation.util.DateUtils;
import com.scube.chargingstation.util.StaticPathContUtils;
import com.scube.chargingstation.util.StringNullEmpty;

import org.springframework.beans.factory.annotation.Value;


public class ChargingRequestMapper {
	
	public static ChargingRequestRespDto toChargingRequestRespDto(ChargingRequestEntity chargingRequestEntity) {
		
		double originalKwh = chargingRequestEntity.getFinalKwh();
		
		String roundOfKwh = String.format("%.2f", originalKwh);
		
		double originalFinalAmt = chargingRequestEntity.getFinalAmount();
		
		String roundOfFinalAmt = String.format("%.2f", originalFinalAmt);
		
        return new ChargingRequestRespDto()
        		.setChargePoint(StringNullEmpty.stringNullAndEmptyToBlank(chargingRequestEntity.getChargingPointEntity().getChargingPointId()))
        		.setChargePointAddr(StringNullEmpty.stringNullAndEmptyToBlank(chargingRequestEntity.getChargingPointEntity().getAddress()))
        		.setActualAmt(roundOfFinalAmt)
        		.setChargedKwh(roundOfKwh)
        		.setChargingTime(String.valueOf(chargingRequestEntity.getChargingTime()))
        		.setConnector(StringNullEmpty.stringNullAndEmptyToBlank(chargingRequestEntity.getConnectorEntity().getChargerTypeEntity().getName()))
        		.setRequestedAmount(chargingRequestEntity.getRequestAmount())
        		.setVehicleNo(StringNullEmpty.stringNullAndEmptyToBlank(chargingRequestEntity.getVehicleNO()));
	} 
	
	public static List<ChargingRequestRespDto> toChargingRequestRespDtos(List<ChargingRequestEntity> chargingRequestEntities) {
		
		List<ChargingRequestRespDto> chargingRequestRespDtos = new ArrayList<ChargingRequestRespDto>();
		
		for(ChargingRequestEntity chargingRequestEntity : chargingRequestEntities) {
			chargingRequestRespDtos.add(toChargingRequestRespDtos(chargingRequestEntity));
		}
		
		return chargingRequestRespDtos;
	}
	
	public static ChargingRequestRespDto toChargingRequestRespDtos(ChargingRequestEntity chargingRequestEntity) { 
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss aa");
		
		double originalKwh = chargingRequestEntity.getFinalKwh();
		
		String roundOfKwh = String.format("%.2f", originalKwh);
		
		double originalFinalAmt = chargingRequestEntity.getFinalAmount();
		
		String roundOfFinalAmt = String.format("%.2f", originalFinalAmt);
		
		Instant chargeStartTime = chargingRequestEntity.getStartTime();
		Date chargeDateInstantToDate = Date.from(chargeStartTime);
		
		String convertDateToString = simpleDateFormat.format(chargeDateInstantToDate);
		
		Instant chargeEndTime = chargingRequestEntity.getStopTime();
		Date chargeEndDateInstantToDate = Date.from(chargeEndTime);
		
		String convertEndDateToString = simpleDateFormat.format(chargeEndDateInstantToDate);
		
		return new ChargingRequestRespDto()  
				.setChargePoint(StringNullEmpty.stringNullAndEmptyToBlank(chargingRequestEntity.getChargingPointEntity().getChargingPointId()))
				.setChargePointAddr(StringNullEmpty.stringNullAndEmptyToBlank(chargingRequestEntity.getChargingPointEntity().getAddress()) + 
						StringNullEmpty.stringNullAndEmptyToBlank(chargingRequestEntity.getChargingPointEntity().getAddress2()) + 
						StringNullEmpty.stringNullAndEmptyToBlank(chargingRequestEntity.getChargingPointEntity().getPincode()))
				.setActualAmt(roundOfFinalAmt)
				.setChargedKwh(roundOfKwh)
				.setVehicleNo(StringNullEmpty.stringNullAndEmptyToBlank(chargingRequestEntity.getVehicleNO()))
				.setName(StringNullEmpty.stringNullAndEmptyToBlank(chargingRequestEntity.getChargingPointEntity().getName()))
	//			.setChargePointAddr(chargingRequestEntity.getChargingPointEntity().getAddress2())
	//			.setChargePointAddr(chargingRequestEntity.getChargingPointEntity().getPincode())
				.setInvoiceFilePath(StaticPathContUtils.APP_URL_DIR+StaticPathContUtils.SET_RECEIPT_FILE_URL_DIR +chargingRequestEntity.getId())
				.setMobileNo(StringNullEmpty.stringNullAndEmptyToBlank(chargingRequestEntity.getMobileNo()))
				.setStartTime(convertDateToString)
				.setChargingTime(StringNullEmpty.stringNullAndEmptyToBlank(chargingRequestEntity.getChargingTime()))
				.setStopTime(convertEndDateToString)
				.setCustName(StringNullEmpty.stringNullAndEmptyToBlank(chargingRequestEntity.getCustName()))
				.setMobileNo(StringNullEmpty.stringNullAndEmptyToBlank(chargingRequestEntity.getMobileNo()))
				.setCgst(chargingRequestEntity.getFinalAmountCGST())
				.setSgst(chargingRequestEntity.getFinalAmountSGST())
				.setAmount(chargingRequestEntity.getFinalAmountWithOutGst())
				//not implemented only for show
				.setIgst(0);       
	}
	
}
