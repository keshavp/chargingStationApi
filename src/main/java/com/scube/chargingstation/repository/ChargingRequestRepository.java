package com.scube.chargingstation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.scube.chargingstation.entity.ChargingPointEntity;
import com.scube.chargingstation.entity.ChargingRequestEntity;
import com.scube.chargingstation.entity.ConnectorEntity;
import com.scube.chargingstation.entity.UserInfoEntity;

@Repository
public interface ChargingRequestRepository extends JpaRepository<ChargingRequestEntity, String>  {

//	ChargingRequestEntity findByChargePointIdAndConnectorId(String chargePointId, int connectorId);

//	ChargingRequestEntity findByChargePointIdAndConnectorIdAndStatus(String chargePointId, int connectorId,String string);

	ChargingRequestEntity findByChargingPointEntityAndConnectorEntityAndStatus(ChargingPointEntity chargingPointEntity,
			ConnectorEntity connectorEntity, String string);

	List<ChargingRequestEntity> findByChargingStatus(String string);
	
	
	List<ChargingRequestEntity> findByChargingPointEntityAndConnectorEntityAndUserInfoEntityAndChargingStatus(ChargingPointEntity
	  chargingPointEntity, ConnectorEntity connectorEntity,UserInfoEntity userInfoEntity ,String status);
	
	
	//@Query(value = "SELECT * FROM emp_info_otp where mobilenumber =(?1)  and otp_code = (?2)  and now() BETWEEN DATE_ADD(created_at , INTERVAL 0 MINUTE) AND DATE_ADD(created_at , INTERVAL 10 MINUTE)   order by created_at limit 1;", nativeQuery = true)
	@Query(value = "SELECT * from charging_request where fk_charging_point=(?1) and fk_connector=(?2) and  fk_user=(?3)  and charging_status in ('Pending','Started')",nativeQuery = true)
	
	List<ChargingRequestEntity> findMyOnGoingChargingProcesses(ChargingPointEntity
			  chargingPointEntity, ConnectorEntity connectorEntity,UserInfoEntity userInfoEntity);
	 

}
