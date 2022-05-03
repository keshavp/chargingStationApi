package com.scube.chargingstation.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "mst_car_model")
@Setter @Getter
public class CarModelEntity extends BaseEntity {

	private String model;
	private String description;
	private String imagePath;
	
	 @JoinTable
	 @ManyToMany
	 private Set<ChargerTypeEntity> chargertypes;
	
	
}
