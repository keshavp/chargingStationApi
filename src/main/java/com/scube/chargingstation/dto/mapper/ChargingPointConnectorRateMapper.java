package com.scube.chargingstation.dto.mapper;

import com.scube.chargingstation.dto.ChargingPointConnectorRateDto;
import com.scube.chargingstation.entity.ChargingPointConnectorRateEntity;

public class ChargingPointConnectorRateMapper {

	public static ChargingPointConnectorRateDto toChargingPointConnectorRateDto(ChargingPointConnectorRateEntity chargingPointEntity) {
		// TODO Auto-generated method stub
		return new ChargingPointConnectorRateDto()
				.setAmount(chargingPointEntity.getAmount())
	//			.setChargingPointCode(null)
	//			.setConnectorCode(null)
				.setKWh(chargingPointEntity.getKWh());
	}

}
