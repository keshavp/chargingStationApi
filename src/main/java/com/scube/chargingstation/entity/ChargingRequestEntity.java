package com.scube.chargingstation.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "charging_request")
@Setter @Getter
public class ChargingRequestEntity extends BaseEntity {

	@Column(name = "charge_point_id")
	private String  chargePointId;
	@Column(name = "connector_id")
	private int  connectorId;
	@Column(name = "mobile_user_Id")
	private String  mobileUser_Id;
	@Column(name = "status")
	private String  status;
	@Column(name = "request_amount")
	private String  requestAmount;
	
}