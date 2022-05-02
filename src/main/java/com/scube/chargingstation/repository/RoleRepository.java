package com.scube.chargingstation.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scube.chargingstation.entity.RoleEntity;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, String> {

	  RoleEntity findByNameCode(String namecode);
	  
	  RoleEntity findByName(String name);
	  
	  Optional<RoleEntity> findById(String id);
	  
	  List<RoleEntity> findByStatusAndIsdeleted(String status ,String isdeleted );

	RoleEntity getRoleById(String id);	

}