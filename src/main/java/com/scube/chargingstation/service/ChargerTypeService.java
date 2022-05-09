package com.scube.chargingstation.service;

import java.util.Set;

import com.scube.chargingstation.dto.ChargerTypeDto;
import com.scube.chargingstation.entity.ChargerTypeEntity;

public interface ChargerTypeService {

	Set<ChargerTypeDto> findActiveChargerType();
	ChargerTypeEntity findByName(String name);
	ChargerTypeEntity findById(String id);

}
