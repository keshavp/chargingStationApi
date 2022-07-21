package com.scube.chargingstation.service;

import java.util.List;

import com.scube.chargingstation.dto.PaymentFrequencyDto;
import com.scube.chargingstation.entity.PaymentFrequencyEntity;

public interface PaymentFrequencyService {

	
	PaymentFrequencyEntity	findByNameCode(String code);

	List<PaymentFrequencyDto> findAllActive();
	
}
