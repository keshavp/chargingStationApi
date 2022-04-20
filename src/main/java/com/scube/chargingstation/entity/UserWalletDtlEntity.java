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
	private String  amount;
	
	@Column(name = "transaction_type")
	private String  transactionType;
	
	@Column(name = "transaction_id")
	private String  transaction_id;

}