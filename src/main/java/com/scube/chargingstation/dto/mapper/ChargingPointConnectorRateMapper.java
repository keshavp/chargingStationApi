package com.scube.chargingstation.dto.mapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.scube.chargingstation.dto.ChargerTypeDto;
import com.scube.chargingstation.dto.ChargingPointConnectorRateDto;
import com.scube.chargingstation.dto.incoming.PriceMasterDto;
import com.scube.chargingstation.entity.ChargerTypeEntity;
import com.scube.chargingstation.entity.ChargingPointConnectorRateEntity;

public class ChargingPointConnectorRateMapper {

	public static ChargingPointConnectorRateDto toChargingPointConnectorRateDto(ChargingPointConnectorRateEntity chargingpointconnectorRateEntity) {
		// TODO Auto-generated method stub
		return new ChargingPointConnectorRateDto()
				.setAmount(chargingpointconnectorRateEntity.getAmount())
			//	.setChargingPointCode(chargingPointEntity.)
	        //	.setConnectorCode(chargingPointEntity.getChargingPointEntity())
				.setKWh(chargingpointconnectorRateEntity.getKwh());
	}

	
	  public static List<PriceMasterDto> topriceMasterDtos(List<PriceMasterDto> psd) {
	  
	  List<PriceMasterDto> priceMasterDtos = new ArrayList<PriceMasterDto>();
	  for(PriceMasterDto chargingpointconnectorRateEntity : psd) {
	  //priceMasterDtos.add(topriceMasterDtos(chargingpointconnectorRateEntity)); }
	    
	  }
	  	return priceMasterDtos; 
	  }
	  
	public static List<ChargingPointConnectorRateDto> toChargingPointConnectorRateDtos(List<ChargingPointConnectorRateEntity> chargingPointConnectorRateEntities) {
 		
		List<ChargingPointConnectorRateDto> chargingPointConnectorRateDtos = new ArrayList<ChargingPointConnectorRateDto>();
		for(ChargingPointConnectorRateEntity chargingpointconnectorRateEntity : chargingPointConnectorRateEntities) {
			chargingPointConnectorRateDtos.add(toChargingPointConnectorRateDto(chargingpointconnectorRateEntity)); 
		}
		
        return chargingPointConnectorRateDtos;
	}

	
}

