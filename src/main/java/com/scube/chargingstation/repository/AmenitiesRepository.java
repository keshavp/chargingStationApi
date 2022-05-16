package com.scube.chargingstation.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scube.chargingstation.entity.AmenitiesEntity;

@Repository
public interface AmenitiesRepository extends JpaRepository<AmenitiesEntity, String>{
		
	// AmenitiesEntity findAll(String name);
		
	// List<AmenitiesEntity> findAll(String name);
	//List<AmenitiesEntity> findByName(String name);
	
	AmenitiesEntity findByName(String name);
	
	List<AmenitiesEntity> findByIsdeleted(String isdeleted);
	
	Optional<AmenitiesEntity> findById(String id);
	
	AmenitiesEntity getAmenitiesById(String id);
}