package com.scube.chargingstation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.scube.chargingstation.entity.ChargingPointEntity;
import com.scube.chargingstation.entity.ConnectorEntity;

@Repository
public interface ConnectorRepository extends JpaRepository<ConnectorEntity, String>  {

	ConnectorEntity findByConnectorIdAndChargingPointEntity(String chargingPointName,
			ChargingPointEntity chargingPointEntity);

	ConnectorEntity findByIdAndChargingPointEntity(String id, ChargingPointEntity chargingPointEntity);

	ConnectorEntity findByConnectorId(String chargerId);
	
	List<ConnectorEntity> findByChargingPointEntity(ChargingPointEntity chargingPointEntity);

	@Query(value = "SELECT * FROM mst_charging_point_connector where connectorId = (?1);",nativeQuery = true)
	List<ConnectorEntity> getConnectorByChargingPointEntity(String chargingPointEntity);

}
