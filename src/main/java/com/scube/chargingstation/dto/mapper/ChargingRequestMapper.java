package com.scube.chargingstation.dto.mapper;

import java.util.HashSet;
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
	
}
