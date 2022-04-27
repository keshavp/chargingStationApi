package com.scube.chargingstation.dto.mapper;

import com.scube.chargingstation.dto.ChargingPointDto;
import com.scube.chargingstation.dto.ChargingStatusRespDto;
import com.scube.chargingstation.entity.ChargingPointEntity;

public class ChargingStatusMapper {
	
	public static ChargingStatusRespDto toChargingStatusRespDto(ChargingPointEntity chargingPointEntity) {
		// TODO Auto-generated method stub
		return new ChargingStatusRespDto()
				.setKwhUnit(0.0);
								
	}
}
