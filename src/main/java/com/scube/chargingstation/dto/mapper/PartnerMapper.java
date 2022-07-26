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
        		.setEmail(partnerInfoEntity.getEmail())
        		.setGstn(partnerInfoEntity.getGstn())
        		.setStatus(partnerInfoEntity.getStatus())
        		.setPercent(partnerInfoEntity.getPercent())
        		.setPymtMode(partnerInfoEntity.getPaymentModeEntity().getNameCode())
        		.setBnfName(partnerInfoEntity.getBnfName())
        		.setBeneAccNo(partnerInfoEntity.getBeneAccNo())
        		.setBeneIfsc(partnerInfoEntity.getBeneIfsc())
        		.setPaymentFrequency(partnerInfoEntity.getPaymentFrequencyEntity().getNameCode());
	}
	public static List<PartnerDto> toPartnersDtos(List<PartnerInfoEntity> partnerInfoEntities) {
		
		List<PartnerDto> partnerIncomingDtos = new ArrayList<PartnerDto>();
		for(PartnerInfoEntity partnerInfoEntity : partnerInfoEntities) {
			partnerIncomingDtos.add(toPartnerDto(partnerInfoEntity));
		}
		return partnerIncomingDtos;
	}

}
