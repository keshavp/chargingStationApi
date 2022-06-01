   package com.scube.chargingstation.service;

 import java.util.List;
 import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scube.chargingstation.dto.ChargerTypeDto;
import com.scube.chargingstation.dto.mapper.ChargerTypeMapper;
import com.scube.chargingstation.entity.ChargerTypeEntity;
import com.scube.chargingstation.repository.ChargerTypeRepository;

@Service
public class ChargerTypeServiceImpl implements ChargerTypeService{
	
	@Autowired
	ChargerTypeRepository chargertyperepository;

	public Set<ChargerTypeDto>findActiveChargerType(){
    Set<ChargerTypeEntity> ChargerTypeEntities= chargertyperepository.findByIsdeleted("N");
    return ChargerTypeMapper.toChargerTypeDto(ChargerTypeEntities);
		
	}

	@Override
	public ChargerTypeEntity findByName(String name) {
		// TODO Auto-generated method stub
		return chargertyperepository.findByName(name);
	}

	@Override
	public ChargerTypeEntity findById(String id) {
		// TODO Auto-generated method stub
		
		ChargerTypeEntity chargerTypeEntity=chargertyperepository.findById(id).get();
		return chargerTypeEntity;
	}

}
