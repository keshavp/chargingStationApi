package com.scube.chargingstation.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.scube.chargingstation.entity.SaveAllChargerStatusDataEntity;

@Repository
public interface SaveAllChargerStatusDataRepository extends JpaRepository<SaveAllChargerStatusDataEntity, String> {
	
	// By Charging Point ID
	@Query(value = "SELECT charging_point_id, charging_point_name, charging_point_status, connector_id, connector_status, count, GROUP_CONCAT(created_at order by created_at desc) as "
			+ "created_at_update FROM all_charger_status where charging_point_id=(?1) and DATE_FORMAT(created_at, '%d%m%Y') and created_at>=(?2) and "
			+ "created_at<=(?3) group by count, connector_id;",nativeQuery = true)
	List<Map<String, String>> getChargerStatusDataByChargingPointIdAndDateRange(String chargingPointId, String startDate, String endDate);

	// For All 
	@Query(value = "SELECT charging_point_id, charging_point_name, charging_point_status, connector_id, connector_status, count, GROUP_CONCAT(created_at order by created_at desc) as "
			+ "created_at_update FROM all_charger_status where DATE_FORMAT(created_at, '%d%m%Y') and created_at>=(?1) and "
			+ "created_at<=(?2) group by count, connector_id;",nativeQuery = true)
	List<Map<String, String>> getAllChargerStatusDataByDateRange(String startDate, String endDate);
	
	
	// Check for Duplicate Records Entry
	@Query(value = "SELECT * FROM all_charger_status where charging_point_id=(?1) and connector_id=(?2) "
			+ "order by created_at desc limit 1;", nativeQuery = true)
	SaveAllChargerStatusDataEntity checkIfDuplicateRecordsInserted(String chargingPointId, String connectorId);
	
}
