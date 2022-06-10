package com.scube.chargingstation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scube.chargingstation.entity.ChargepointForSeverEntity;

@Repository
public interface ChargepointForSeverRepository  extends JpaRepository<ChargepointForSeverEntity, String>  {

	ChargepointForSeverEntity findByChargePointId(String chargingPointId);

}
