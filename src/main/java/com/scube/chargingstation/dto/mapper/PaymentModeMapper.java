package com.scube.chargingstation.dto.mapper;
import java.util.ArrayList;
import java.util.List;

import com.scube.chargingstation.dto.PaymentModeDto;
import com.scube.chargingstation.entity.PaymentModeEntity;

public class PaymentModeMapper {
	public static PaymentModeDto toPaymentModeDto(PaymentModeEntity paymentModeEntity) {
	 return new PaymentModeDto()
				//  .setId(paymentModeEntity.getId())
	        		.setName(paymentModeEntity.getName())
	        	//	.setStatus(paymentModeEntity.getStatus())
	                 .setNameCode(paymentModeEntity.getNameCode());
	        

	}
public static List<PaymentModeDto> toPaymentModeDtos(List<PaymentModeEntity> paymentModeEntities) {
 		
		List<PaymentModeDto> paymentModeDtos = new ArrayList<PaymentModeDto>();
		for(PaymentModeEntity paymentModeEntity : paymentModeEntities) {
			paymentModeDtos.add((PaymentModeDto) toPaymentModeDto(paymentModeEntity)); 
		}
        return paymentModeDtos;
	}


}