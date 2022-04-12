package com.scube.chargingstation.service;

import java.util.Map;

import com.scube.chargingstation.dto.ConnectorDto;
import com.scube.chargingstation.dto.incoming.UserWalletRequestDto;
import com.scube.chargingstation.entity.ChargingPointEntity;
import com.scube.chargingstation.entity.ConnectorEntity;

public interface UserPaymentService {

	public boolean processWalletMoney(UserWalletRequestDto userWalletRequestDto);
	
	public Map<String, String>  getMyWalletBalance(UserWalletRequestDto userWalletRequestDto);

}
