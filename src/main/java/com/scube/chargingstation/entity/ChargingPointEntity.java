package com.scube.chargingstation.entity;

import java.time.Instant;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "mst_charging_point")
public class ChargingPointEntity  extends BaseEntity {

	private String chargingPointId;
	
	@OneToMany(mappedBy="chargingPointEntity" , cascade = CascadeType.ALL)
    private Set<ConnectorEntity> connectorEntities;
	
	
	public String getChargingPointId() {
		return chargingPointId;
	}

	public void setChargingPointId(String chargingPointId) {
		this.chargingPointId = chargingPointId;
	}

	public Set<ConnectorEntity> getConnectorEntities() {
		return connectorEntities;
	}

	public void setConnectorEntities(Set<ConnectorEntity> connectorEntities) {
		this.connectorEntities = connectorEntities;

	    for(ConnectorEntity b : connectorEntities) {
	        b.setChargingPointEntity(this);
	    }
	}

}
