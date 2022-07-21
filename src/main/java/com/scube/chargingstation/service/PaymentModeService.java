package com.scube.chargingstation.service;

import java.util.List;

import com.scube.chargingstation.dto.PaymentModeDto;
import com.scube.chargingstation.entity.PaymentModeEntity;

public interface PaymentModeService {

	PaymentModeEntity	findByNameCode(String code);
	
	List<PaymentModeDto> findAllActive();
}
