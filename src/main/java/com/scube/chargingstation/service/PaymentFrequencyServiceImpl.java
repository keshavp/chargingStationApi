package com.scube.chargingstation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scube.chargingstation.dto.PaymentFrequencyDto;
import com.scube.chargingstation.dto.mapper.PaymentFrequencyMapper;
import com.scube.chargingstation.entity.PaymentFrequencyEntity;
import com.scube.chargingstation.exception.BRSException;
import com.scube.chargingstation.exception.EntityType;
import com.scube.chargingstation.exception.ExceptionType;
import com.scube.chargingstation.repository.PaymentFrequencyRepository;

@Service
public class PaymentFrequencyServiceImpl implements PaymentFrequencyService {

	@Autowired
	PaymentFrequencyRepository frequencyRepository;
	
	@Override
	public PaymentFrequencyEntity findByNameCode(String code) {
		// TODO Auto-generated method stub
		
		PaymentFrequencyEntity paymentFrequencyEntity = frequencyRepository.findByNameCode(code);
		
		if(paymentFrequencyEntity == null) {
			 throw BRSException.throwException(EntityType.PAYMENTFREQUENCY,ExceptionType.ENTITY_NOT_FOUND , code);
		}
		
		return paymentFrequencyEntity;
	}

	@Override
	public List<PaymentFrequencyDto> findAllActive() {
		// TODO Auto-generated method stub
		
		 List<PaymentFrequencyEntity> paymentFrequencyEntity = frequencyRepository.findByStatus("ACTIVE");
		
		 if(paymentFrequencyEntity.size() == 0 ) {
			 throw BRSException.throwException(EntityType.PAYMENTFREQUENCY,ExceptionType.ENTITY_NOT_FOUND , "");
		 }
		 
		return PaymentFrequencyMapper.toPaymentFrequencyDtos(paymentFrequencyEntity);
	}

}
