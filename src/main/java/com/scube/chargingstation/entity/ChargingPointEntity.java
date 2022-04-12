package com.scube.chargingstation.entity;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.JoinTable;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "mst_charging_point")
public class ChargingPointEntity  extends BaseEntity {

	private String chargingPointId;
	private String address;
	private String logitude;
	private String distance;
	private String latitude;
	private String name;
	private String rating;
	private String status;
	
	
	@OneToMany(mappedBy="chargingPointEntity" , cascade = CascadeType.ALL)
    private Set<ConnectorEntity> connectorEntities;
	
	 @JoinTable
	    @OneToMany
	    private Set<AmenitiesEntity> amenities;
	
	
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLogitude() {
		return logitude;
	}

	public void setLogitude(String logitude) {
		this.logitude = logitude;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Set<AmenitiesEntity> getAmenities() {
		return amenities;
	}

	public void setAmenities(Set<AmenitiesEntity> amenities) {
		this.amenities = amenities;
	}

	
	
	
	
	
	
	
	
}
