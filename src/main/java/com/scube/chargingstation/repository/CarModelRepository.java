package com.scube.chargingstation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scube.chargingstation.entity.CarModelEntity;
import com.scube.chargingstation.entity.ChargerTypeEntity;

@Repository
public interface CarModelRepository extends JpaRepository<CarModelEntity, String>  {


	


	
}



