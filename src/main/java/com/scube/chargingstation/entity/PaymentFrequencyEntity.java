package com.scube.chargingstation.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "mst_payment_frequency")
@Getter @Setter
public class PaymentFrequencyEntity extends BaseEntity{
	
	private String name;
	@Column(name="name_code")
	private String nameCode;
	private String status;
	
}