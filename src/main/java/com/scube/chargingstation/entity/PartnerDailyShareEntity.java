package com.scube.chargingstation.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "partner_daily_share")
@Setter @Getter
public class PartnerDailyShareEntity extends BaseEntity {

	
	@OneToOne
	@JoinColumn(name = "fk_partner")
	private PartnerInfoEntity partner;
	
	@Column(name = "amount", columnDefinition = "double default 0")
	private double  amount;
	
	@Column(name = "total_kwh", columnDefinition = "double default 0")
	private double  totalKwh;
	
	@Column(name = "payment_status")
	private String  paymentStatus;
	
	@Column(name = "invoice_file_path")
	private String  invoiceFilePath;
	
	private double percent;
	
}
