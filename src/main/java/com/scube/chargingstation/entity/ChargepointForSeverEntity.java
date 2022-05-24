package com.scube.chargingstation.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "chargepoint")
@Setter @Getter
public class ChargepointForSeverEntity {

	@Id
	@Column(name = "ChargePointId")
	private String chargePointId;
	@Column(name = "Name")
	private String name;
	@Column(name = "Comment")
	private String comment;
	@Column(name = "Username")
	private String username;
	@Column(name = "Password")
	private String password;
	@Column(name = "ClientCertThumb")
	private String clientCertThumb;
}
