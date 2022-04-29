package com.scube.chargingstation.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "emp_cars")
@Setter @Getter
public class UserCarsEntity extends BaseEntity{
	
	@OneToOne
    @JoinColumn(name = "fk_user")
    private UserInfoEntity userInfoEntity;
	

	@OneToOne
    @JoinColumn(name = "fk_carmodel")
    private CarModelEntity carModelEntity;
	
	private String vehicleNo;
	
}