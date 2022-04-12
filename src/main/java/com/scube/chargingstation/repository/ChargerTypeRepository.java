package com.scube.chargingstation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scube.chargingstation.entity.ChargerTypeEntity;
import com.scube.chargingstation.entity.ChargingPointEntity;

@Repository
public interface ChargerTypeRepository extends JpaRepository<ChargerTypeEntity, String>  {

//	ChargingPointEntity findByChargingPointId(String connectorName);

}
