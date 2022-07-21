package com.scube.chargingstation.service;

import java.util.List;

import javax.validation.Valid;

import com.scube.chargingstation.dto.PartnerDto;
import com.scube.chargingstation.dto.incoming.PartnerIncomingDto;
import com.scube.chargingstation.entity.PartnerInfoEntity;

public interface PartnerService {
	
	boolean addPartner(@Valid PartnerIncomingDto partnerIncomingDto);
	
	boolean editPartner(@Valid PartnerIncomingDto partnerIncomingDto);
	
	List<PartnerDto> getAllPartners();
	
	PartnerDto getPartnerUserById(String id);
	
	List<PartnerDto> getAllActivePartners();
	
	PartnerInfoEntity getPartnersById(String id);
	
	PartnerInfoEntity getPartnerById(String id);

	boolean deletePartnerUserById(String id);
	
	boolean addPartnerDailyShare();

}
