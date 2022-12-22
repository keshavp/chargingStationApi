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

	ChargingPointConnectorRateEntity findByChargingPointEntityAndConnectorEntityAndAmount(ChargingPointEntity chargingPointEntity, ConnectorEntity connectorEntity, double amount);
	
	ChargingPointConnectorRateEntity findByChargingPointEntityAndConnectorEntityAndKwh(ChargingPointEntity chargingPointEntity, ConnectorEntity connectorEntity, double kwh);

	ChargingPointConnectorRateEntity findByChargingPointEntity(ChargingPointEntity chargingPointEntity);
	
	Optional<ChargingPointConnectorRateEntity> findById(String id);
	
	@Query(value = "SELECT * FROM mst_charging_point_connector_calculation group by fk_charging_point ,fk_connector;", nativeQuery=true )
	List<ChargingPointConnectorRateEntity> getAllAddedConnectorRateGroupByChargingPointEntityAndConnectEntity();
	
	@Query(value = "SELECT * FROM mst_charging_point_connector_calculation where fk_charging_point=(?1) and fk_connector=(?2) and kWh = '1';", nativeQuery=true )
	ChargingPointConnectorRateEntity getEntityByChargingPointIdAndConnectorIdAndKwh1(ChargingPointEntity chargingPointEntity, ConnectorEntity connectorEntity);
	
	@Query(value = "SELECT max(amount) as amount FROM mst_charging_point_connector_calculation where fk_charging_point=(?1) and fk_connector=(?2);", nativeQuery = true)
	Double getMaxAmountFromEntity(ChargingPointEntity chargingPointEntity, ConnectorEntity connectorEntity);
	
	@Query(value = "SELECT * FROM mst_charging_point_connector_calculation tbl1 where "
			+ "tbl1.fk_charging_point=(?1) and tbl1.fk_connector=(?2) and (amount >=(?3)) order by amount limit 1;", nativeQuery = true)
	ChargingPointConnectorRateEntity getBookingAmountForRequestedAmountKwhAndTime(ChargingPointEntity chargingPointEntity, ConnectorEntity connectorEntity, Double amount);
	
	@Query(value = "SELECT * FROM  mst_charging_point_connector_calculation where fk_charging_point = (?1) and fk_connector = (?2);", nativeQuery=true )
	List<ChargingPointConnectorRateEntity> getConnectorRateByChargingPointEntityAndConnectEntity(String chargingPoint, String connector);

	@Query(value = "SELECT * FROM mst_charging_point_connector_calculation where fk_charging_point  = (?1) and fk_connector = (?2) and status =(?3) group by fk_charging_point ,fk_connector,status limit 1;", nativeQuery=true )
	ChargingPointConnectorRateEntity findByChargingPointEntityAndConnectorEntityAndStatusGroupByWithLimit(String chargingPoint, String connector, String status);
}
