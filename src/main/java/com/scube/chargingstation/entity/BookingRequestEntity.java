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
	
	@Column(name = "request_amount", columnDefinition = "double default 0")
	private double  requestAmount;
	
	@Column(name = "requested_kwh", columnDefinition = "double default 0")
	private double requestedKwh;
	
	@Column(name = "requested_time")
	private String requestedTime;
	
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
	private ConnectorEntity connectorEntity;
	
	@OneToOne
    @JoinColumn(name = "fk_charge_request")
	private ChargingRequestEntity chargingRequestEntity;
	
	@Column (name = "booking_time")
	private Instant bookingTime;
	
	@Column (name = "booking_preference_type")
	private String bookingPreferenceType;
	
	@Column(name = "cust_name")
	private String  custName;
	
	@Column(name = "mobile_no")
	private String  mobileNo;
	
	@Column(name = "vehicle_no")
	private String  vehicleNO;
	
	@Column (name = "booking_endtime")
	private Instant bookingEndtime;
	
	@Column (name = "invoice_file_path")
	private String invoiceFilePath;
	
	@Column (name = "receipt_no")
	private String receiptNo;
	
	@Column (name = "day_reminder_status")
	private String oneDayReminderStatus;
	
	@Column (name = "day_reminder_time")
	private String oneDayReminderTime;
	
	@Column (name = "hour_reminder_status")
	private String hourReminderStatus;
	
	@Column (name = "hour_reminder_time")
	private String hourReminderTime;
}
