package com.scube.chargingstation.entity;

import java.time.Instant;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "mst_charger_type")
@Setter @Getter
public class ChargerTypeEntity  extends BaseEntity {


	private String name;
	private String imagePath;
	
}
