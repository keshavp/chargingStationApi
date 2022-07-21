package com.scube.chargingstation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scube.chargingstation.entity.AmenitiesEntity;
import com.scube.chargingstation.entity.PartnerDailyShareEntity;

@Repository
public interface PartnerDailyShareRepository extends JpaRepository<PartnerDailyShareEntity, String>{

}
