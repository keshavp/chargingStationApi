package com.scube.chargingstation.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "mst_partner_users")
@Getter
@Setter
public class PartnerInfoEntity extends BaseEntity{
	
//	private String partnerId;
	private String partnerName;
	private String mobileno;
	private String alternateMobileNo;
	private String email;
	
	private String address1;
	private String address2;
	private String pincode;
	
	private String gstn;
	private String status;
	
}
