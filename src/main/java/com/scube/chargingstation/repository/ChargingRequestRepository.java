package com.scube.chargingstation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scube.chargingstation.entity.ChargingRequestEntity;

@Repository
public interface ChargingRequestRepository extends JpaRepository<ChargingRequestEntity, Long>  {

	ChargingRequestEntity findByChargePointIdAndConnectorId(String chargePointId, int connectorId);

	ChargingRequestEntity findByChargePointIdAndConnectorIdAndStatus(String chargePointId, int connectorId,String string);
	
	

}
