package com.scube.chargingstation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scube.chargingstation.entity.ChargingPointEntity;
import com.scube.chargingstation.entity.ConnectorEntity;

@Repository
public interface ConnectorRepository extends JpaRepository<ConnectorEntity, String>  {

	ConnectorEntity findByConnectorIdAndChargingPointEntity(String chargingPointName,
			ChargingPointEntity chargingPointEntity);

	ConnectorEntity findByIdAndChargingPointEntity(String id, ChargingPointEntity chargingPointEntity);

}
