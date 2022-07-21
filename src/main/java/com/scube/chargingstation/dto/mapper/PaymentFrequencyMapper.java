package com.scube.chargingstation.dto.mapper;
import java.util.ArrayList;
import java.util.List;

import com.scube.chargingstation.dto.PaymentFrequencyDto;
import com.scube.chargingstation.entity.PaymentFrequencyEntity;

public class PaymentFrequencyMapper {
	public static PaymentFrequencyDto toPaymentFrequencyDto(PaymentFrequencyEntity paymentFrequencyEntity) {
	 return new PaymentFrequencyDto()
				//  .setId(paymentFrequencyEntity.getId())
	        		.setName(paymentFrequencyEntity.getName())
	        	//	.setStatus(paymentFrequencyEntity.getStatus())
	                 .setNameCode(paymentFrequencyEntity.getNameCode());
	        

	}
public static List<PaymentFrequencyDto> toPaymentFrequencyDtos(List<PaymentFrequencyEntity> paymentFrequencyEntities) {
 		
		List<PaymentFrequencyDto> paymentFrequencyDtos = new ArrayList<PaymentFrequencyDto>();
		for(PaymentFrequencyEntity paymentFrequencyEntity : paymentFrequencyEntities) {
			paymentFrequencyDtos.add((PaymentFrequencyDto) toPaymentFrequencyDto(paymentFrequencyEntity)); 
		}
        return paymentFrequencyDtos;
	}


}