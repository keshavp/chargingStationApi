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
	
	@OneToOne
    @JoinColumn(name = "fk_transactions")
    private TransactionsEntity transactionsEntity;
	
	@Column(name = "status")
	private String  status;
	
	@Column(name = "request_amount", columnDefinition = "double default 0")
	private double  requestAmount;
	
	@Column(name = "request_kwh", columnDefinition = "double default 0")
	private double  requestKwh;
	
	@Column(name = "difference_kwh", columnDefinition = "double default 0")
	private double  differenceKwh;
	
	@Column(name = "difference_amount", columnDefinition = "double default 0")
	private double  differenceAmount;
	
	@Column(name = "final_amount", columnDefinition = "double default 0")
	private double  finalAmount;
	
	@Column(name = "final_kwh", columnDefinition = "double default 0")
	private double  finalKwh;
	
	@Column(name = "amount_credit_debit_status")
	private String  amountCrDrStatus;
	
	@Column(name = "StartTime")
	private Instant  startTime;
	
	@Column(name = "MeterStart" , columnDefinition = "double default 0")
	private double  meterStart;

	@Column(name = "StopTime")
	private Instant  stopTime;

	@Column(name = "MeterStop" , columnDefinition = "double default 0")
	private double  meterStop;
	
	@Column(name = "MeterStopAmount" , columnDefinition = "double default 0")
	private double  meterStopAmount;
	
	@Column(name = "charging_status")
	private String  chargingStatus;
	
	@Column(name = "invoice_file_path")
	private String  invoiceFilePath;
	
	
	@Column(name = "cust_name")
	private String  custName;
	
	@Column(name = "mobile_no")
	private String  mobileNo;
	
	@Column(name = "vehicle_no")
	private String  vehicleNO;
	
	@Column(name = "receipt_no")
	private String  receiptNo;
	
	@Column(name = "charging_time", columnDefinition = "00:00:00")
	private String  chargingTime;
	
}