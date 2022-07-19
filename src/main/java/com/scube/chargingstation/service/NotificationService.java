package com.scube.chargingstation.service;

import com.scube.chargingstation.dto.incoming.NotificationReqDto;
import com.scube.chargingstation.dto.incoming.UserWalletRequestDto;
import com.scube.chargingstation.entity.BookingRequestEntity;

public interface NotificationService {

	
	public boolean  sendNotification(NotificationReqDto notificationReqDto);

	public boolean  sendBookingReminderNotification(BookingRequestEntity  bookingRequestEntity);
	
}
