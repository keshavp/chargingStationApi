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
@Table(name = "charging_request")
@Setter @Getter
public class ChargingRequestEntity extends BaseEntity {

	
	@OneToOne
    @JoinColumn(name = "fk_charging_point")
    private ChargingPointEntity chargingPointEntity;
	
	@OneToOne
    @JoinColumn(name = "fk_connector")
    private ConnectorEntity connectorEntity;
	
	@OneToOne
    @JoinColumn(name = "fk_user")
    private UserInfoEntity userInfoEntity;
	
	@Column(name = "status")
	private String  status;
	
	@Column(name = "request_amount")
	private String  requestAmount;
	
	@OneToOne
    @JoinColumn(name = "fk_transactions")
    private TransactionsEntity transactionsEntity;
	
	@Column(name = "StartTime")
	private Instant  startTime;
	
	@Column(name = "MeterStart" , columnDefinition = "double default 0")
	private double  meterStart;

	@Column(name = "StopTime")
	private Instant  stopTime;

	@Column(name = "MeterStop" , columnDefinition = "double default 0")
	private double  meterStop;
	
	@Column(name = "charging_status")
	private String  chargingStatus;
	
	@Column(name = "invoice_file_path")
	private String  invoiceFilePath;
	
}