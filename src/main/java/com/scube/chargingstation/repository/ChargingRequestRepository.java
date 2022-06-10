package com.scube.chargingstation.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.annotation.JacksonInject.Value;
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
	
	
	
	

	 @Query(value = "SELECT * FROM charging_request where fk_charging_point = (?1) and DATE_FORMAT(StartTime, '%d%m%Y') "
	 		+ " between DATE_FORMAT(STR_TO_DATE((?2), '%d-%M-%Y'), '%d%m%Y') and DATE_FORMAT(STR_TO_DATE((?3), '%d-%M-%Y'), '%d%m%Y')",nativeQuery = true)
	List<ChargingRequestEntity> findByChargingPointEntity(String chargingPoint , String startDate, String endDate);

	 @Query(value = "SELECT IFNULL(sum(final_kwh),0) as kwh FROM charging_request where charging_status = 'Done' and DATE_FORMAT(created_at, '%Y%m%d') = DATE_FORMAT(DATE_SUB(now(), INTERVAL 1 DAY), '%Y%m%d');", nativeQuery = true)
	 int getYesterdayConsumedKwh();

	 @Query(value = "SELECT IFNULL(sum(final_kwh),0) as kwh FROM charging_request where charging_status = 'Done' and created_at between DATE_SUB(now(), INTERVAL 7 DAY) and now();", nativeQuery = true)
	 int getWeekConsumedKwh();

	 @Query(value = "SELECT IFNULL(sum(cr.final_kwh),0) as kwh ,IFNULL(cp.name,'') as name FROM charging_request cr left join mst_charging_point cp on cr.fk_charging_point = cp.id group by cr.fk_charging_point order by cr.created_at , kwh;", nativeQuery = true)
	 List<Map<String, String>> getMostActiveChargingStations();

	 @Query(value = "SELECT SEC_TO_TIME(IFNULL(SUM( TIME_TO_SEC(charging_time) ),'00:00:00')) AS timeSum FROM charging_request where charging_status = 'Done' and created_at between DATE_SUB(now(), INTERVAL 30 DAY) and now()", nativeQuery = true)
	 String get30daysTotalChargingTime();
	 
	 @Query(value = "SELECT IFNULL(count(id),0) AS timeSum FROM charging_request where charging_status = 'Done' and created_at between DATE_SUB(now(), INTERVAL 7 DAY) and now()", nativeQuery = true)
	 int weekTotalChargingRequestCountSessions();
	 
//	ChargingRequestEntity findByChargingPointEntity(ChargingPointEntity chargingPointEntity);

//	ChargingRequestEntity findByChargingPointId(ChargingPointEntity chargingPointEntity);
	 
//	 
	
	 @Query(value = "SELECT IFNULL(sum(cr.final_kwh),0) as  totalRecharge , IFNULL(sum(cr.final_amount),0) as totalAmountSpent , cr.fk_user , IFNULL(ew.current_balance,0) as walletBalance  FROM charging_request cr left join emp_wallet ew on cr.fk_user = ew.fk_user  where cr.fk_user = (?1);", nativeQuery = true)
	 Map<String, String> getUserChargingRequestDetails(String id);
	 
	 @Query(value = "SELECT *  FROM charging_request where fk_user = (?1) order by created_at desc limit 1;", nativeQuery = true)
	 ChargingRequestEntity getRecentReharge(String id);
}



