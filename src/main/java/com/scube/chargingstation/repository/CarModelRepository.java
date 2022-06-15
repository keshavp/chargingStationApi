package com.scube.chargingstation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scube.chargingstation.entity.CarModelEntity;
import com.scube.chargingstation.entity.ChargerTypeEntity;
import com.scube.chargingstation.entity.PartnerInfoEntity;

@Repository
public interface CarModelRepository extends JpaRepository<CarModelEntity, String>  {


	
	List<CarModelEntity> findByStatus(String status);


	
}



