package com.scube.chargingstation.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scube.chargingstation.entity.ChargerTypeEntity;
import com.scube.chargingstation.entity.ChargingPointEntity;

@Repository
public interface ChargerTypeRepository extends JpaRepository<ChargerTypeEntity, String>  {

	Set<ChargerTypeEntity> findByIsdeleted(String string);

//	ChargingPointEntity findByChargingPointId(String connectorName);
	ChargerTypeEntity findByName(String name);

	Set<ChargerTypeEntity> findByIsdeletedAndStatus(String staus, String isdeleted);
}
