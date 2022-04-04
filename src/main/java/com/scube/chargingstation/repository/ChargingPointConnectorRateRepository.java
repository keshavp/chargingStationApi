package com.scube.chargingstation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scube.chargingstation.entity.ChargingPointConnectorRateEntity;
import com.scube.chargingstation.entity.ChargingPointEntity;
import com.scube.chargingstation.entity.ConnectorEntity;

@Repository
public interface ChargingPointConnectorRateRepository extends JpaRepository<ChargingPointConnectorRateEntity, Long>  {

	ChargingPointConnectorRateEntity findByChargingPointEntityAndConnectorEntity(ChargingPointEntity chargingPointEntity, ConnectorEntity connectorEntity);

	ChargingPointConnectorRateEntity findByChargingPointEntityAndConnectorEntityAndAmount(
			ChargingPointEntity chargingPointEntity, ConnectorEntity connectorEntity, String amount);
}
