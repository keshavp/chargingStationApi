package com.scube.chargingstation.dto.mapper;

import java.nio.charset.IllegalCharsetNameException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;

import com.scube.chargingstation.dto.ChargingPointConnectorDto;
import com.scube.chargingstation.dto.ChargingRequestRespDto;
import com.scube.chargingstation.entity.ChargingRequestEntity;
import com.scube.chargingstation.entity.ConnectorEntity;
import com.scube.chargingstation.util.StaticPathContUtils;

import org.springframework.beans.factory.annotation.Value;


public class ChargingRequestMapper {
	
	public static ChargingRequestRespDto toChargingRequestRespDto(ChargingRequestEntity chargingRequestEntity) {
		
        return new ChargingRequestRespDto()
        		.setChargePoint(chargingRequestEntity.getChargingPointEntity().getChargingPointId())
        		.setChargePointAddr(chargingRequestEntity.getChargingPointEntity().getAddress())
        		.setActualAmt(chargingRequestEntity.getFinalAmount())
        		.setChargedKwh(chargingRequestEntity.getFinalKwh())
        		.setChargingTime(String.valueOf(chargingRequestEntity.getChargingTime()))
        		.setConnector(chargingRequestEntity.getConnectorEntity().getChargerTypeEntity().getName())
        		.setRequestedAmount(chargingRequestEntity.getRequestAmount())
        		.setVehicleNo(chargingRequestEntity.getVehicleNO());
	} 
	
	public static List<ChargingRequestRespDto> toChargingRequestRespDtos(List<ChargingRequestEntity> chargingRequestEntities) {
		
		List<ChargingRequestRespDto> chargingRequestRespDtos = new ArrayList<ChargingRequestRespDto>();
		
		for(ChargingRequestEntity chargingRequestEntity : chargingRequestEntities) {
			chargingRequestRespDtos.add(toChargingRequestRespDtos(chargingRequestEntity));
		}
		
		return chargingRequestRespDtos;
	}
	
	public static ChargingRequestRespDto toChargingRequestRespDtos(ChargingRequestEntity chargingRequestEntity) {  
		
		return new ChargingRequestRespDto()  
				.setChargePoint(chargingRequestEntity.getChargingPointEntity().getChargingPointId())
				.setChargePointAddr(chargingRequestEntity.getChargingPointEntity().getAddress() + chargingRequestEntity.getChargingPointEntity().getAddress2() + chargingRequestEntity.getChargingPointEntity().getPincode())
				.setActualAmt(chargingRequestEntity.getFinalAmount())
				.setChargedKwh(chargingRequestEntity.getFinalKwh())
				.setVehicleNo(chargingRequestEntity.getVehicleNO())
				.setName(chargingRequestEntity.getChargingPointEntity().getName())
	//			.setChargePointAddr(chargingRequestEntity.getChargingPointEntity().getAddress2())
	//			.setChargePointAddr(chargingRequestEntity.getChargingPointEntity().getPincode())
				.setMobileNo(chargingRequestEntity.getMobileNo())
				.setStartTime(chargingRequestEntity.getStartTime())
				.setChargingTime(chargingRequestEntity.getChargingTime())
				.setStopTime(chargingRequestEntity.getStopTime())
				.setCustName(chargingRequestEntity.getCustName())
				.setMobileNo(chargingRequestEntity.getMobileNo());
	}
	
}
