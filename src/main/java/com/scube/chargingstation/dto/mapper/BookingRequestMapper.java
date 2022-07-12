package com.scube.chargingstation.dto.mapper;


import java.util.Date;
import java.util.List;

import com.scube.chargingstation.dto.BookingSlotsRespDto;

public class BookingRequestMapper {
	
	public static BookingSlotsRespDto toBookingSlotsRespDto (Date slot) {
		return new BookingSlotsRespDto();
	//			.setSlotDateAndTime(slot);
		
	}

}
