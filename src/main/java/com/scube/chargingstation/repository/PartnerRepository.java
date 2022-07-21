package com.scube.chargingstation.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.scube.chargingstation.entity.PartnerInfoEntity;

@Repository
public interface PartnerRepository extends JpaRepository<PartnerInfoEntity, String>{
	
	Optional<PartnerInfoEntity> findById(String id);
	
	List<PartnerInfoEntity> findByStatus(String status);
	
	 @Query(value = "select mcp.fk_partner partnerId,mpu.partnerName,sum(cr.final_amount) Amount,sum(cr.final_kwh) totalKwh,DATE_FORMAT( cr.StartTime, '%Y-%m-%d') chargingDate from charging_request cr ,mst_charging_point mcp ,mst_partner_users mpu where cr.fk_charging_point=mcp.id " + 
	 		" and mcp.fk_partner=mpu.id and cr.charging_status='Done' and DATE_FORMAT( cr.StartTime, '%Y-%m-%d') = DATE_FORMAT(now(), '%Y-%m-%d') group by mcp.fk_partner", nativeQuery = true)
	 List<Map<String, String>> getPartnerDailyShare();
}
