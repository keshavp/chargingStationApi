package com.scube.chargingstation.dto.mapper;

import java.util.ArrayList;
import java.util.List;

import com.scube.chargingstation.dto.PartnerDto;
import com.scube.chargingstation.entity.PartnerInfoEntity;

public class PartnerMapper {
	
	public static PartnerDto toPartnerDto(PartnerInfoEntity partnerInfoEntity) {
 		
        return new PartnerDto()
        		.setPartnerName(partnerInfoEntity.getPartnerName())
        		.setId(partnerInfoEntity.getId())
        		.setAddress1(partnerInfoEntity.getAddress1())
        		.setAddress2(partnerInfoEntity.getAddress2())
        		.setPincode(partnerInfoEntity.getPincode())
        		.setMobileno(partnerInfoEntity.getMobileno())
        		.setAlternateMobileNo(partnerInfoEntity.getAlternateMobileNo())
        		.setGstn(partnerInfoEntity.getGstn())
        		.setStatus(partnerInfoEntity.getStatus());
	}
	
	
	public static List<PartnerDto> toPartnersDtos(List<PartnerInfoEntity> partnerInfoEntities) {
		
		List<PartnerDto> partnerIncomingDtos = new ArrayList<PartnerDto>();
		for(PartnerInfoEntity partnerInfoEntity : partnerInfoEntities) {
			partnerIncomingDtos.add(toPartnerDto(partnerInfoEntity));
		}
		return partnerIncomingDtos;
	}

}
