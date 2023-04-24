package com.scube.chargingstation.entity;

import java.time.Instant;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "daily_emp_wallet")
@Setter @Getter
public class DailyUserWalletEntity {
	
	@Id
	@GeneratedValue(generator = "custom-generator", strategy = GenerationType.IDENTITY)
	@GenericGenerator(
			name = "custom-generator",
			strategy = "com.scube.chargingstation.model.id.generator.BaseIdentifierGenerator")
	private String id;
	
	@Column(name = "id1")
	private String id1;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false, nullable = false)
	private Instant createdAt;

	@Column
	@Version
	private int version;
	
	@Column(name = "is_deleted")
	private String isdeleted;
	
	@UpdateTimestamp
	@Column(name = "modified_at")
	private Instant modifiedAt;
	
	
	@OneToOne
    @JoinColumn(name = "fk_user")
    private UserInfoEntity userInfoEntity;
	

	@Column(name = "current_balance")
	private Double  currentBalance;
	
	
	@Column(name = "latest_date")
	private String  latest_Date;
}
