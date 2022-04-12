package com.scube.chargingstation.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "emp_info_otp")
@Setter @Getter
public class UserInfoOtpEntity extends BaseEntity{
	
	@OneToOne
    @JoinColumn(name = "fk_userInfo")
	private UserInfoEntity userInfoEntity;
	
	private String mobilenumber;
	@Column(name = "otp_code")
	private String otpCode;
	
	private String status;
}