package com.scube.chargingstation.service;

import com.scube.chargingstation.dto.incoming.NotificationReqDto;
import com.scube.chargingstation.dto.incoming.UserWalletRequestDto;

public interface NotificationService {

	
	public boolean  sendNotification(NotificationReqDto notificationReqDto);

}
