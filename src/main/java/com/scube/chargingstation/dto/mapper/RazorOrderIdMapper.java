package com.scube.chargingstation.dto.mapper;

import com.scube.chargingstation.dto.RazorOrderIdDto;

public class RazorOrderIdMapper {

	public static RazorOrderIdDto toRazorOrderIdDto(String orderId) {
 		
        return new RazorOrderIdDto()
        		.setOrderId(orderId);
    }
}
