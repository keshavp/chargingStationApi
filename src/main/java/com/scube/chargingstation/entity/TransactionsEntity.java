package com.scube.chargingstation.entity;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "transactions")
@Setter @Getter
public class TransactionsEntity {
	
	@Id
	@Column(name = "TransactionId")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int  transactionId; 
	@Column(name = "Uid")
	private String  uid;
	@Column(name = "ChargePointId")
	private String  chargePointId;
	@Column(name = "ConnectorId")
	private int  connectorId;
	@Column(name = "StartTagId")
	private String  startTagId;
	@Column(name = "StartTime")
	private Instant  startTime;
	@Column(name = "MeterStart")
	private double  meterStart;
	@Column(name = "StartResult")
	private String  startResult;
	@Column(name = "StopTagId")
	private String  stopTagId;
	@Column(name = "StopTime")
	private Instant  stopTime;
	@Column(name = "MeterStop")
	private double  meterStop;
	@Column(name = "StopReason")
	private String  stopReason;
	@Column(name = "AllowedCharge")
	private double  allowedCharge;
	@Column(name = "LastMeter", columnDefinition = "double default 0")
	private double  lastMeter;
	
}
