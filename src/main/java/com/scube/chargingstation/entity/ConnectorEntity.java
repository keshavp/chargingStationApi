package com.scube.chargingstation.entity;
import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "mst_charging_point_connector")
@Setter @Getter
public class ConnectorEntity extends BaseEntity {

	private String connectorId;
	
	@ManyToOne
    @JoinColumn(name="chargingPointEntity_id", nullable=false)
    private ChargingPointEntity chargingPointEntity;
}
