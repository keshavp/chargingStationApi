package com.scube.chargingstation.entity;
import java.time.Instant;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
		
	@OneToOne
    @JoinColumn(name = "fk_chargertype")
    private ChargerTypeEntity chargerTypeEntity;
	
	@OneToOne
    @JoinColumn(name = "fk_connectorstatus")
    private ConnectorStatusEntity connectorStatusEntity;
	
	@Column (name = "slot_intervals")
	private String slotInterval;
}
