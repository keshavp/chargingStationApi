package com.scube.chargingstation.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "complaint_response")
@Getter
@Setter
public class ComplainResponseEntity extends BaseEntity {
	
	private String comments;
	
	@ManyToOne
	@JoinColumn (name = "complaint_id", nullable = false)
	private ComplaintEntity complaintEntity;
	
	@OneToOne
    @JoinColumn(name = "close_fk_user")
	private UserInfoEntity closeUserEntity;
	
}
