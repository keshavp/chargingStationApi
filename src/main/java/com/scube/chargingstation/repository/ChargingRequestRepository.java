package com.scube.chargingstation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scube.chargingstation.entity.ChargingPointEntity;
import com.scube.chargingstation.entity.ChargingRequestEntity;
import com.scube.chargingstation.entity.ConnectorEntity;

@Repository
public interface ChargingRequestRepository extends JpaRepository<ChargingRequestEntity, Long>  {

//	ChargingRequestEntity findByChargePointIdAndConnectorId(String chargePointId, int connectorId);

//	ChargingRequestEntity findByChargePointIdAndConnectorIdAndStatus(String chargePointId, int connectorId,String string);

	ChargingRequestEntity findByChargingPointEntityAndConnectorEntityAndStatus(ChargingPointEntity chargingPointEntity,
			ConnectorEntity connectorEntity, String string);

	List<ChargingRequestEntity> findByChargingStatus(String string);
	
	/*
	 * ChargingRequestEntity
	 * findByChargingPointEntityAndConnectorEntityAndStatus(ChargingPointEntity
	 * chargingPointEntity, ConnectorEntity connectorEntity, String string);
	 */

}
