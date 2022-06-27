package com.scube.chargingstation.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.scube.chargingstation.entity.ChargingPointConnectorRateEntity;
import com.scube.chargingstation.entity.ChargingPointEntity;
import com.scube.chargingstation.entity.ConnectorEntity;

@Repository
public interface ChargingPointConnectorRateRepository extends JpaRepository<ChargingPointConnectorRateEntity, Long>  {

	ChargingPointConnectorRateEntity findByChargingPointEntityAndConnectorEntity(ChargingPointEntity chargingPointEntity, ConnectorEntity connectorEntity);

	ChargingPointConnectorRateEntity findByChargingPointEntityAndConnectorEntityAndAmount(
			ChargingPointEntity chargingPointEntity, ConnectorEntity connectorEntity, double amount);
	
	ChargingPointConnectorRateEntity findByChargingPointEntityAndConnectorEntityAndKwh(
			ChargingPointEntity chargingPointEntity, ConnectorEntity connectorEntity, double kwh);

	ChargingPointConnectorRateEntity findByChargingPointEntity(ChargingPointEntity chargingPointEntity);
	
	Optional<ChargingPointConnectorRateEntity> findById(String id);
	
	@Query(value = "SELECT * FROM mst_charging_point_connector_calculation group by fk_charging_point ,fk_connector;", nativeQuery=true )
	List<ChargingPointConnectorRateEntity> getAllAddedConnectorRateGroupByChargingPointEntityAndConnectEntity();
	
//	ChargingPointConnectorRateEntity findByPricingDetailsId(String id);
}
