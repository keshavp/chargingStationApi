package com.scube.chargingstation.entity;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "refreshtoken")
public class RefreshToken extends BaseEntity {

	
	  @OneToOne
	  @JoinColumn(name = "user_id", referencedColumnName = "id")
	  private UserInfoEntity user;

	  @Column(nullable = false, unique = true)
	  private String token;

	  @Column(name= "expiry_date", nullable = false)
	  private Instant expiryDate;
}
