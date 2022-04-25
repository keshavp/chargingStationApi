package com.scube.chargingstation.service;

import java.util.List;

import javax.validation.Valid;

import com.scube.chargingstation.dto.RoleDto;
import com.scube.chargingstation.dto.incoming.RoleIncomingDto;
import com.scube.chargingstation.entity.RoleEntity;

public interface RoleService {

	
	RoleEntity	findRoleNameByCode(String code);
	RoleEntity	findRoleId(String id);

	boolean addRole(@Valid RoleIncomingDto roleIncomingDto);

	boolean editRole(@Valid RoleIncomingDto roleIncomingDto);
	boolean deleteRole(String id);
	 List<RoleDto>  findAllRoles();
	 List<RoleDto> findActiveRoles();
	

	
}
