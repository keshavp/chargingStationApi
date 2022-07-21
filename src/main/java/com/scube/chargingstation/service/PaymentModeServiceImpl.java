package com.scube.chargingstation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scube.chargingstation.dto.PaymentModeDto;
import com.scube.chargingstation.dto.mapper.PaymentModeMapper;
import com.scube.chargingstation.entity.PaymentModeEntity;
import com.scube.chargingstation.exception.BRSException;
import com.scube.chargingstation.exception.EntityType;
import com.scube.chargingstation.exception.ExceptionType;
import com.scube.chargingstation.repository.PaymentModeRepository;

@Service
public class PaymentModeServiceImpl implements PaymentModeService {

	@Autowired
	PaymentModeRepository paymentModeRepository;
	
	
	@Override
	public PaymentModeEntity findByNameCode(String code) {
		PaymentModeEntity paymentModeEntity = paymentModeRepository.findByNameCode(code);
		
		if(paymentModeEntity == null) {
			 throw BRSException.throwException(EntityType.PAYMENTMODE,ExceptionType.ENTITY_NOT_FOUND , code);
		}
		
		return paymentModeEntity;
	}


	@Override
	public List<PaymentModeDto> findAllActive() {
		// TODO Auto-generated method stub
		
		List<PaymentModeEntity> paymentModeEntity = paymentModeRepository.findByStatus("ACTIVE");
		
		if(paymentModeEntity.size() == 0 ) {
			 throw BRSException.throwException(EntityType.PAYMENTMODE,ExceptionType.ENTITY_NOT_FOUND , "");
		}
		
		return PaymentModeMapper.toPaymentModeDtos(paymentModeEntity);
	}
}