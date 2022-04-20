package com.scube.chargingstation.entity;
import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "mst_charging_point_connector_calculation")
@Setter @Getter
public class ChargingPointConnectorRateEntity extends BaseEntity {

	private double amount;
	private double kwh;
	private String time;
	
	@OneToOne
    @JoinColumn(name = "fk_charging_point")
    private ChargingPointEntity chargingPointEntity;
	
	@OneToOne
    @JoinColumn(name = "fk_connector")
    private ConnectorEntity connectorEntity;
}