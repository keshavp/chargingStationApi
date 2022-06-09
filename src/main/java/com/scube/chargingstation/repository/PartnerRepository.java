package com.scube.chargingstation.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scube.chargingstation.entity.PartnerInfoEntity;

@Repository
public interface PartnerRepository extends JpaRepository<PartnerInfoEntity, String>{
		
	Optional<PartnerInfoEntity> findById(String id);
	
	List<PartnerInfoEntity> findByStatus(String status);
}
