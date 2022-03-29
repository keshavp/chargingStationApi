package com.scube.chargingstation.service;

import com.scube.chargingstation.entity.RoleEntity;

public interface RoleService {

	
	RoleEntity	findRoleNameByCode(String code);	
	
}
