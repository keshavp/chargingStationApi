package com.scube.chargingstation.entity;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;


import lombok.Getter;
import lombok.Setter;

@Entity
@Table (name = "booking_request")
@Getter
@Setter
public class BookingRequestEntity extends BaseEntity{
	
	@Column (name = "booking_amount", columnDefinition = "double default 0")
	private double bookingAmount;
	
	@Column (name = "booking_status")
	private String bookingStatus;
	
	@OneToOne
    @JoinColumn(name = "fk_user")
	private UserInfoEntity userInfoEntity;

	@OneToOne
    @JoinColumn(name = "fk_charging_point")
	private ChargingPointEntity chargingPointEntity;
	
	@OneToOne
    @JoinColumn(name = "fk_connector")
	private ConnectorEntity chargerTypeEntity;
	
	@OneToOne
    @JoinColumn(name = "fk_charge_request")
	private ChargingRequestEntity chargingRequestEntity;
	
	@Column (name = "booking_time")
	private Instant bookingTime;
	
}
