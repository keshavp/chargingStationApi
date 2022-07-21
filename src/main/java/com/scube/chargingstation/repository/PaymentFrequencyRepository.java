package com.scube.chargingstation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scube.chargingstation.entity.PaymentFrequencyEntity;

@Repository
public interface PaymentFrequencyRepository extends JpaRepository<PaymentFrequencyEntity, String>{

	PaymentFrequencyEntity findByNameCode(String code);

	List<PaymentFrequencyEntity> findByStatus(String string);

}
