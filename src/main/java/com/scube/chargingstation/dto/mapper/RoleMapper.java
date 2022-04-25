package com.scube.chargingstation.dto.mapper;


import java.util.ArrayList;
import java.util.List;

import com.scube.chargingstation.dto.RoleDto;
import com.scube.chargingstation.entity.RoleEntity;

public class RoleMapper {
	public static RoleDto toRoleDto(RoleEntity roleEntity) {
	 return new RoleDto()
				  .setId(roleEntity.getId())
	        		.setName(roleEntity.getName())
	        		.setStatus(roleEntity.getStatus());
	        

	}
public static List<RoleDto> toRoleDtos(List<RoleEntity> roleEntities) {
 		
		List<RoleDto> roleDtos = new ArrayList<RoleDto>();
		for(RoleEntity roleEntity : roleEntities) {
			roleDtos.add((RoleDto) toRoleDto(roleEntity)); 
		}
        return roleDtos;
	}


}
