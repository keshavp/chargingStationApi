package com.scube.chargingstation.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "mst_payment_mode")
@Getter @Setter
public class PaymentModeEntity extends BaseEntity{
	
	private String name;
	@Column(name="name_code")
	private String nameCode;
	private String status;
	
}