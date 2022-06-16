  package com.scube.chargingstation.service;

import static com.scube.chargingstation.exception.ExceptionType.ALREADY_EXIST;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scube.chargingstation.dto.RoleDto;
import com.scube.chargingstation.dto.incoming.RoleIncomingDto;
import com.scube.chargingstation.dto.mapper.RoleMapper;
import com.scube.chargingstation.entity.RoleEntity;
import com.scube.chargingstation.exception.BRSException;
import com.scube.chargingstation.exception.EntityType;
import com.scube.chargingstation.repository.RoleRepository;


@Service
   public class RoleServiceImpl implements RoleService{
	
	private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

	@Autowired
	RoleRepository	roleRepository;
	
	@Override
	public boolean addRole(@Valid RoleIncomingDto roleIncomingDto) {
		// TODO Auto-generated method stub
		
		logger.info("********RoleServiceImpl addRole********");
		
		if(roleIncomingDto.getName() == "" ) {
			
			throw BRSException.throwException("Role name can't be blank");
		}
		
		if(roleIncomingDto.getNamecode() == "") {
			
			throw BRSException.throwException("Role Code can't be blank");
		}
		
		if(roleIncomingDto.getStatus() == "") {
			
			throw BRSException.throwException("Role status can't be blank");
		}
		
		RoleEntity roleCodeDuplicateCheck = roleRepository.findByNameCode(roleIncomingDto.getNamecode());
		if(roleCodeDuplicateCheck != null) {
			
			logger.error("throw error that user already exists for NameCode = "+ roleIncomingDto.getNamecode());
			throw BRSException.throwException(EntityType.ROLE, ALREADY_EXIST, roleIncomingDto.getNamecode());
		}
		RoleEntity usernmeDuplicateCheck = roleRepository.findByName(roleIncomingDto.getName());
		if(usernmeDuplicateCheck != null) {
			
			logger.error("throw error that user already exists for RoleName = "+ roleIncomingDto.getName());
			throw BRSException.throwException(EntityType.ROLE, ALREADY_EXIST, roleIncomingDto.getName());
		}
		
		RoleEntity roleEntity = new RoleEntity();
		roleEntity.setName(roleIncomingDto.getName());
		roleEntity.setNameCode(roleIncomingDto.getNamecode());
		roleEntity.setStatus(roleIncomingDto.getStatus());
		roleEntity.setIsdeleted("N");
		
		roleRepository.save(roleEntity);

		return true;
	}


	@Override
	public RoleEntity findRoleNameByCode(String code) {
		// TODO Auto-generated method stub
   RoleEntity roleEntity = roleRepository.findByNameCode(code);
		
		return roleEntity;	
		
	}

	public RoleEntity findRoleId(String id) {
		// TODO Auto-generated method stub
     RoleEntity roleEntity = roleRepository.findById(id).get();
		
		return roleEntity;	
		

	}

	@Override
	public boolean editRole(@Valid RoleIncomingDto roleIncomingDto) {
		// TODO Auto-generated method stub
		
	 if(roleIncomingDto.getId() == "" || roleIncomingDto.getId() ==null ) {
				
				throw BRSException.throwException("something went wrong");
	 } 
			
	   if(roleIncomingDto.getName() == "" || roleIncomingDto.getName() ==null ) {
			
			throw BRSException.throwException("Role name can't be blank or null");
		}
		
	   if(roleIncomingDto.getNamecode() == "" || roleIncomingDto.getNamecode() ==null ) {
			
			throw BRSException.throwException("Role Name code can't be blank or null");
		}
		
		/*
		 * if(roleIncomingDto.getStatus() == "" || roleIncomingDto.getStatus() ==null )
		 * {
		 * 
		 * throw BRSException.throwException("Role namecode can't be blank or null"); }
		 */
	  	
		RoleEntity roleEntity = roleRepository.findById(roleIncomingDto.getId()).get();
		
		
		
		roleEntity.setName(roleIncomingDto.getName());
		roleEntity.setNameCode(roleIncomingDto.getNamecode());
	//	roleEntity.setStatus(roleIncomingDto.getStatus());
		roleRepository.save(roleEntity);
		return true;
	}


	@Override
	public boolean deleteRole(String id) {
		// TODO Auto-generated method stub
		RoleEntity roleEntity = roleRepository.findById(id).get();
		
		if(roleEntity.getId() == "" || roleEntity.getId() ==null ) {
			
			throw BRSException.throwException("Role id can't be blank or null");
		}
		
		/*
		 * roleEntity.setIsdeleted("Y"); roleEntity.setStatus("INACTIVE");
		 * roleRepository.save(roleEntity);
		 */
		
		 roleRepository.delete(roleEntity);
		
		return true;
	}


	@Override
	public  List<RoleDto> findAllRoles() {
		// TODO Auto-generated method stub
		
	    List<RoleEntity> roleEntities = roleRepository.findAll();
		return  RoleMapper.toRoleDtos(roleEntities);
	}


	@Override
	public List<RoleDto> findActiveRoles() {
		// TODO Auto-generated method stub
		List<RoleEntity> roleEntities = roleRepository.findByStatusAndIsdeleted("Active","N");
		return  RoleMapper.toRoleDtos(roleEntities);
	}


	@Override
	public RoleDto getRoleById(String id) {
		// TODO Auto-generated method stub
		
		RoleEntity roleEntity = roleRepository.getRoleById(id);
		return RoleMapper.toRoleDto(roleEntity);
	}
}
