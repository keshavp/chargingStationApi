package com.scube.chargingstation.service;

import java.security.InvalidKeyException;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.scube.chargingstation.dto.ChargingHistoryDto;
import com.scube.chargingstation.dto.ChargingHistoryRespDto;
import com.scube.chargingstation.dto.ConnectorDto;
import com.scube.chargingstation.dto.RazorOrderIdDto;  
import com.scube.chargingstation.dto.incoming.UserWalletRequestDto;
import com.scube.chargingstation.entity.ChargingPointEntity;
import com.scube.chargingstation.entity.ConnectorEntity;

public interface UserPaymentService {

	public boolean processWalletMoney(UserWalletRequestDto userWalletRequestDto);
	
	public Map<String, String>  getMyWalletBalance(UserWalletRequestDto userWalletRequestDto);

	public RazorOrderIdDto addWalletMoneyRequest(UserWalletRequestDto userWalletRequestDto);

	//boolean addUserWalletDetail(UserWalletRequestDto userWalletRequestDto);

	String createRazorOrderID(UserWalletRequestDto userWalletRequestDto);

	boolean verifyRazorSignature(UserWalletRequestDto userWalletRequestDto) ;

	public boolean addWalletMoneyTransaction(UserWalletRequestDto userWalletRequestDto) ;

	List<ChargingHistoryRespDto> getChargingTrHistory (UserWalletRequestDto userWalletRequestDto) ;
	
}
