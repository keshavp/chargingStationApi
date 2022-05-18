package com.scube.chargingstation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scube.chargingstation.entity.ChargingPointEntity;

@Repository
public interface ChargingPointRepository extends JpaRepository<ChargingPointEntity, String>  {

	ChargingPointEntity findByChargingPointId(String connectorName);

	List<ChargingPointEntity> findByStatus(String status);

	List<ChargingPointEntity> findByIsdeleted(String string);

}
