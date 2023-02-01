package com.scube.chargingstation.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "all_charger_status")
@Getter @Setter
public class SaveAllChargerStatusDataEntity extends BaseEntity {
	
	@Column(name = "charging_point_id")
	private String chargingPointId;

	@Column(name = "charging_point_name")
	private String chargingPointName;
	
	@Column(name = "charging_point_status")
	private String chargingPointStatus;
	
	@Column(name = "connector_id")
	private String connectorID;
	
	@Column(name = "connector_status")
	private String connectorStatus;
	
	@Column(name = "detail")
	private String details;
	
	@Column(name = "count")
	private int count;
	
}
