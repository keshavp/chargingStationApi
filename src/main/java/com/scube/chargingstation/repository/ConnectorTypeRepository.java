package com.scube.chargingstation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scube.chargingstation.entity.ChargerTypeEntity;
import com.scube.chargingstation.entity.RoleEntity;

@Repository
public interface ConnectorTypeRepository extends JpaRepository<ChargerTypeEntity, String>{
	
	ChargerTypeEntity findByName(String Name);
	List<ChargerTypeEntity> findByStatusAndIsdeleted(String status,String isdeleted);
	ChargerTypeEntity getConnectorTypeById(String id);
	

}
