package com.scube.chargingstation.entity;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "mst_charging_point")
public class ChargingPointEntity  extends BaseEntity {

	private String chargingPointId;
	private String address;
	private Double longitude;
	private Double distance;
	private Double latitude;
	private String name;
	private Double rating;
	private String status;
	
	
	@OneToMany(mappedBy="chargingPointEntity" , cascade = CascadeType.ALL)
    private Set<ConnectorEntity> connectorEntities;
	
	 @JoinTable
	    @ManyToMany
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

	

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
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
