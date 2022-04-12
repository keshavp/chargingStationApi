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
@Table(name = "emp_info")
@Setter @Getter
public class UserInfoEntity extends BaseEntity{
	
	private String username;
	private String mobilenumber;
	private String email;
	
	@OneToOne
    @JoinColumn(name = "fk_role")
	private RoleEntity role;
	private String password;
	private String status;
	private String verified;
	private String resetpassword;
	private int resetpasswordcount;
	@UpdateTimestamp
	@Column(name = "reset_password_instance")
	private Instant resetPassInstance;
	
}