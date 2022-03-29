package com.scube.chargingstation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scube.chargingstation.entity.RoleEntity;
import com.scube.chargingstation.repository.RoleRepository;


@Service
public class RoleServiceImpl implements RoleService{

	@Autowired
	RoleRepository	roleRepository;
	
	@Override
	public RoleEntity findRoleNameByCode(String code) {
		// TODO Auto-generated method stub
	
		RoleEntity roleEntity = roleRepository.findByNameCode(code);
		
		return roleEntity;
	}

}
