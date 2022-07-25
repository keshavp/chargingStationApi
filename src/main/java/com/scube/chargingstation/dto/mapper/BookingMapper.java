package com.scube.chargingstation.dto.mapper;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.scube.chargingstation.dto.BookingResponseDto;
import com.scube.chargingstation.entity.BookingRequestEntity;
import com.scube.chargingstation.util.DateUtils;

public class BookingMapper {
	
	public static BookingResponseDto toBookingResponseDto(BookingRequestEntity bookingRequestEntity) {
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss aa");
		
		Instant userBookingTimeInInstant = bookingRequestEntity.getBookingTime();
		Date userBookingDateTime = Date.from(userBookingTimeInInstant);
		
		String convertBookingTimeIntoString = simpleDateFormat.format(userBookingDateTime);
			
		return new BookingResponseDto()
				.setBookingId(bookingRequestEntity.getId())
				.setBookingDateAndTime(convertBookingTimeIntoString)
				.setCustName(bookingRequestEntity.getCustName())
				.setCustMobileNo(bookingRequestEntity.getMobileNo())
				.setCustVehicleNo(bookingRequestEntity.getVehicleNO())
				.setStationId(bookingRequestEntity.getChargingPointEntity().getChargingPointId())
				.setStationName(bookingRequestEntity.getChargingPointEntity().getName())
				.setConnectorId(bookingRequestEntity.getConnectorEntity().getConnectorId())
				.setConnectorName(bookingRequestEntity.getConnectorEntity().getChargerTypeEntity().getName())
				.setBookingAmount(bookingRequestEntity.getBookingAmount())
				.setRequestedAmount(bookingRequestEntity.getRequestAmount());
		
	}
	
	
	public static List<BookingResponseDto> toBookingResponseDtos(List<BookingRequestEntity> bookingRequestEntities) {
 		
		List<BookingResponseDto> bookingResponseDtos = new ArrayList<BookingResponseDto>();
		for(BookingRequestEntity complaintEntity : bookingRequestEntities) {
			bookingResponseDtos.add(toBookingResponseDto(complaintEntity)); 
		}
        return bookingResponseDtos;
	}
	
}
