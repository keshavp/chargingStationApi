package com.scube.chargingstation.service;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.scube.chargingstation.dto.incoming.ChargingRequestDto;
import com.scube.chargingstation.dto.incoming.ChargingStationDto;
import com.scube.chargingstation.entity.ChargingPointEntity;
import com.scube.chargingstation.entity.ChargingRequestEntity;
import com.scube.chargingstation.entity.ConnectorEntity;
import com.scube.chargingstation.entity.UserWalletEntity;
import com.scube.chargingstation.dto.incoming.UserWalletRequestDto;
import com.scube.chargingstation.dto.response.ChargingPointResponse;
import com.scube.chargingstation.dto.response.Response;


public interface ChargingRequestService {

	
	public boolean addChargingRequest(ChargingRequestDto chargingRequestDto) ;
	
	List<ChargingRequestEntity> findChargingRequestEntityByChargingStatus(String chargingStatus);

	public ChargingRequestEntity findChargingRequestEntityByChargingPointEntityAndConnectorEntityAndStatus(
			ChargingPointEntity chargingPointEntity, ConnectorEntity connectorEntity, String string);
			
	public boolean processWalletMoney(UserWalletRequestDto userWalletRequestDto);
	
	public Map<String, String>  getMyWalletBalance(UserWalletRequestDto userWalletRequestDto);

	public List<ChargingPointResponse> getNearByChargingStations11(ChargingStationDto chargingStationDto);
	
	
			
}
