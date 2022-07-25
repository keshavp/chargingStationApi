package com.scube.chargingstation.entity;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "emp_wallet_Dtl")
@Setter @Getter
public class UserWalletDtlEntity extends BaseEntity{
	
	@OneToOne
    @JoinColumn(name = "fk_user")
    private UserInfoEntity userInfoEntity;
	
	@OneToOne
    @JoinColumn(name = "fk_chargingreq")
    private
	ChargingRequestEntity chargingRequestEntity;
	
	@Column(name = "amount")
	private Double  amount;
	
	@Column(name = "transaction_type")
	private String  transactionType;
	
	@Column(name = "transaction_id")
	private String  transactionId;
	
	@Column(name = "order_id")
	private String  orderId;
	
	@Column(name = "razor_signature")
	private String  razorSignature;
	
	@Column(name = "payment_method")
	private String  paymentMethod;
	
	@Column(name = "current_balance")
	private Double  currentBalance;

}