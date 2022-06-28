package com.scube.chargingstation.dto.mapper;

import java.util.ArrayList;
import java.util.List;

import com.scube.chargingstation.dto.PriceDto;
import com.scube.chargingstation.entity.ChargingPointConnectorRateEntity;

public class PriceMapper {
	
	public static PriceDto toPriceMasterDto(ChargingPointConnectorRateEntity chargingPointConnectorRateEntity) {
		
		return new PriceDto()
				.setId(chargingPointConnectorRateEntity.getId())
				.setChargingStationName(chargingPointConnectorRateEntity.getChargingPointEntity().getName())
				.setConnectorName(chargingPointConnectorRateEntity.getConnectorEntity().getChargerTypeEntity().getName())
				.setPricingDetailsId(chargingPointConnectorRateEntity.getId())
				.setPricingAmount(chargingPointConnectorRateEntity.getAmount())
				.setPricingCgst(chargingPointConnectorRateEntity.getCgst())
				.setChargingAmount(chargingPointConnectorRateEntity.getChargingAmount())
				.setPricingKwh(chargingPointConnectorRateEntity.getKwh())
				.setPricingSgst(chargingPointConnectorRateEntity.getSgst())
				.setPricingTime(chargingPointConnectorRateEntity.getTime())
				.setChargingPointId(chargingPointConnectorRateEntity.getChargingPointEntity().getId())
				.setConnectorId(chargingPointConnectorRateEntity.getConnectorEntity().getId());
	}

	public static PriceDto toPriceDto(ChargingPointConnectorRateEntity chargingPointConnectorRateEntity) {
		
		return new PriceDto()
				.setPricingDetailsId(chargingPointConnectorRateEntity.getId())
				.setChargingStationName(chargingPointConnectorRateEntity.getChargingPointEntity().getName())
				.setConnectorName(chargingPointConnectorRateEntity.getConnectorEntity().getChargerTypeEntity().getName())
				.setChargingPointId(chargingPointConnectorRateEntity.getChargingPointEntity().getId())
				.setConnectorId(chargingPointConnectorRateEntity.getConnectorEntity().getId());
				
	}

	public static List<PriceDto> toPriceDtos(List<ChargingPointConnectorRateEntity> chargingPointConnectorRateEntities) {
		
		List<PriceDto> priceDtos = new ArrayList<PriceDto>();
		for(ChargingPointConnectorRateEntity chargingPointConnectorRateEntity : chargingPointConnectorRateEntities) {
			priceDtos.add(toPriceMasterDto(chargingPointConnectorRateEntity));
		}
		
		return priceDtos;
		
	}
	
}
