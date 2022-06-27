package com.scube.chargingstation.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.scube.chargingstation.entity.ChargingPointEntity;

@Repository
public interface ChargingPointRepository extends JpaRepository<ChargingPointEntity, String>  {

	ChargingPointEntity findByChargingPointId(String connectorName);

	List<ChargingPointEntity> findByStatus(String status);

	List<ChargingPointEntity> findByIsdeleted(String string);
	
	@Query (value = "SELECT * FROM mst_charging_point where fk_partner = (?1);", nativeQuery=true )
	List<ChargingPointEntity> findChargingStationsByPartnerId(String id);
	
	
	@Query(value="select *,FN_GET_DISTANCE((?1),(?2),latitude,longitude) as fardistance from mst_charging_point order by fardistance ",nativeQuery = true)
	List<Map<String, String>> getNearByChargingStations(Double lat,Double longi);

}
