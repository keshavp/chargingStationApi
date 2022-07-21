package com.scube.chargingstation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scube.chargingstation.entity.PaymentModeEntity;


@Repository
public interface PaymentModeRepository extends JpaRepository<PaymentModeEntity, String>{

	PaymentModeEntity findByNameCode(String code);

	List<PaymentModeEntity> findByStatus(String string);

}
