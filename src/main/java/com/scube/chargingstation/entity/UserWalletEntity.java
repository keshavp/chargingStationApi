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
@Table(name = "emp_wallet")
@Setter @Getter
public class UserWalletEntity extends BaseEntity{
	
	@OneToOne
    @JoinColumn(name = "fk_user")
    private UserInfoEntity userInfoEntity;
	

	@Column(name = "current_balance")
	private Double  currentBalance;
	
	
}