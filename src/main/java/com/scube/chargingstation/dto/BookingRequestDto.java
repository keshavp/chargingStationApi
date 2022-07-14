package com.scube.chargingstation.dto;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.scube.chargingstation.entity.ChargingPointEntity;
import com.scube.chargingstation.entity.ChargingRequestEntity;
import com.scube.chargingstation.entity.ConnectorEntity;
import com.scube.chargingstation.entity.UserInfoEntity;

public class BookingRequestDto {

	private double bookingAmount;
	
	private double  requestAmount;
	
	private String bookingStatus;
	
	private UserInfoEntity userInfoEntity;
	private ChargingPointEntity chargingPointEntity;
	
	private ConnectorEntity chargerTypeEntity;
	private ChargingRequestEntity chargingRequestEntity;
	private Instant bookingTime;
	private String  custName;
	private String  mobileNo;
	private String  vehicleNO;
	
}
