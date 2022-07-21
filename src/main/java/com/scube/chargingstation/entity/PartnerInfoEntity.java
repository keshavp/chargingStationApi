package com.scube.chargingstation.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.print.DocFlavor.STRING;

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
	
	@OneToOne
    @JoinColumn(name = "fk_userInfo")
	private UserInfoEntity userInfoEntity;
	
	private String bnfName;
	private String beneAccNo;
	private String beneIfsc;
	private double percent;
	@OneToOne
    @JoinColumn(name = "fk_paymentFrequency")
	private PaymentFrequencyEntity paymentFrequencyEntity;
	
	@OneToOne
    @JoinColumn(name = "fk_PaymentMode")
	private PaymentModeEntity paymentModeEntity;
}
