package com.scube.chargingstation.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
	@Query(value = "SELECT * from charging_request where fk_charging_point=(?1) and fk_connector=(?2) and  fk_user=(?3)  and charging_status in ('Starting')",nativeQuery = true)
	List<ChargingRequestEntity> findMyOnGoingChargingProcesses(ChargingPointEntity
			  chargingPointEntity, ConnectorEntity connectorEntity,UserInfoEntity userInfoEntity);
	 

	//@Query(value = "SELECT * from charging_request where fk_charging_point=(?1) and fk_connector=(?2) and  fk_user=(?3)  and charging_status in ('Pending')",nativeQuery = true)
	@Query(value = "SELECT * from charging_request where fk_charging_point=(?1) and fk_connector=(?2) and charging_status in ('Pending')",nativeQuery = true)
	List<ChargingRequestEntity> findMyPendingChargingRequests(ChargingPointEntity
			  chargingPointEntity, ConnectorEntity connectorEntity);
	
	//,UserInfoEntity userInfoEntity
	
	
	
	@Query(value = " SELECT cr.request_amount as requestedAmount,cr.request_kwh as requestedKwh,cs.ChargeSpeed as chargingSpeed,"
			+ " cs.LastMeter as currentKwh, IFNULL(cs.sOc,'' ) as chargingPercent ,  IFNULL(cr.vehicle_no,'Request') as vehicleNo ,'' as estimatedTime "
			+ " from charging_request cr ,connectorstatus cs "
			+ " where cr.fk_transactions=cs.TransactionId  and  cr.fk_user=(?1)  and cr.charging_status in ('Starting') ",nativeQuery = true)
	List<Map<String, String>> getUserChargingStatus(String userId);
	
	
	@Query(value = "SELECT * from charging_request where charging_status=(?1) and TIMESTAMPDIFF(MINUTE,created_at,NOW()) > (?2) ",nativeQuery = true)
	List<ChargingRequestEntity> findByChargingStatusAndCreatedMinutes(String chargingStatus,String Miniutes);

	List<ChargingRequestEntity> findByChargingPointEntity(ChargingPointEntity chargingPointEntity);


//	ChargingRequestEntity findByChargingPointEntity(ChargingPointEntity chargingPointEntity);

//	ChargingRequestEntity findByChargingPointId(ChargingPointEntity chargingPointEntity);
	
	
}



