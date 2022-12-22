package com.scube.chargingstation.service;

import java.util.List;

import com.scube.chargingstation.dto.ChargingPointDto;
import com.scube.chargingstation.dto.ChargingRequestRespDto;
import com.scube.chargingstation.dto.ChargingStatusRespDto;
import com.scube.chargingstation.dto.MostActiveChargingStationsDto;
import com.scube.chargingstation.dto.UserDashboardDto;
import com.scube.chargingstation.dto.incoming.BookingRequestIncomingDto;
import com.scube.chargingstation.dto.incoming.ChargingRequestDto;
import com.scube.chargingstation.dto.incoming.ChargingStationDto;
import com.scube.chargingstation.dto.incoming.ChargingStationWiseReportIncomingDto;
import com.scube.chargingstation.entity.ChargingPointConnectorRateEntity;
import com.scube.chargingstation.entity.ChargingPointEntity;
import com.scube.chargingstation.entity.ChargingRequestEntity;
import com.scube.chargingstation.entity.ConnectorEntity;
import com.scube.chargingstation.entity.UserInfoEntity;

public interface ChargingRequestService {

	
	public boolean addChargingRequest(ChargingRequestDto chargingRequestDto) ;
	
	List<ChargingRequestEntity> findChargingRequestEntityByChargingStatus(String chargingStatus);

	public ChargingRequestEntity findChargingRequestEntityByChargingPointEntityAndConnectorEntityAndStatus(
			ChargingPointEntity chargingPointEntity, ConnectorEntity connectorEntity, String string);
			
	
	public List<ChargingPointDto> getNearByChargingStations(ChargingStationDto chargingStationDto);
	
	public List<ChargingStatusRespDto> getChargingStatus(ChargingRequestDto chargingRequestDto);

	public void timeoutPendingChargingRequests();
	
	public void sendGunInsertNotification();
	
	public List<ChargingPointDto> getNearByChargingStationsOld(ChargingStationDto chargingStationDto);
	
	public ChargingRequestRespDto getChargingRequestDetails(ChargingRequestDto chargingRequestDto);
	
	public List<ChargingRequestRespDto> getChargingHistoryDetailsByStation(ChargingStationWiseReportIncomingDto chargingStationWiseReportIncomingDto);
	
//	public ChargingRequestRespDto getChargingHistoryByStation(ChargingRequestDto chargingRequestDto); 
	
	
	Double getYesterdayConsumedKwh();
	Double getWeekConsumedKwh();
	String get30daysTotalChargingTime();
	Double weekTotalChargingRequestCountSessions();
	
	List<MostActiveChargingStationsDto> getMostActiveChargingStations();
	
	UserDashboardDto getUserChargingRequestDetails(String id);
	ChargingRequestEntity getRecentReharge(String id);

	public Double getYesterdayConsumedKwhByPartnerId(UserInfoEntity userInfoEntity);

	public Double getWeekConsumedKwhByPartnerId(UserInfoEntity userInfoEntity);

	public String get30daysTotalChargingTimeByPartnerId(UserInfoEntity userInfoEntity);

	public Double weekTotalChargingRequestCountSessionsByPartnerId(UserInfoEntity userInfoEntity);
	
	public Double getChargeAmountForKwhAndTime(Double kwhForTime, ChargingRequestDto chargingRequestDto, 
			ChargingPointConnectorRateEntity chargingPointConnectorRateEntity);
	
	public Double getAllowedCharge(ChargingRequestDto chargingRequestDto, ChargingPointConnectorRateEntity chargingPointConnectorRateEntity);

	public List<MostActiveChargingStationsDto> getMostActiveChargingStationsByPartnerId(UserInfoEntity userInfoEntity);

	//public String callResetConnectorApi(String chargingPointId, String connectorId);
	public boolean chargeNow(ChargingRequestDto chargingRequestDto) ;
	
	public boolean stopChargingOnTime();

	public boolean chargeStop(String id);
	
}
