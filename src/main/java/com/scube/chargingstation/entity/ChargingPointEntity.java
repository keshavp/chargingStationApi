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
	private String name;
	private String partnerName;
	private String startTime;
	private String endTime;
	
	private String address;
	private String address2;
	private String pincode;
	private Double longitude;
	private Double distance;
	private Double latitude;

	private Double rating;
	private String status;
	
	private String manufractures;
	private String communicationtype;
	private String powerstandards;
	
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

	public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getManufractures() {
		return manufractures;
	}

	public void setManufractures(String manufractures) {
		this.manufractures = manufractures;
	}

	public String getCommunicationtype() {
		return communicationtype;
	}

	public void setCommunicationtype(String communicationtype) {
		this.communicationtype = communicationtype;
	}

	public String getPowerstandards() {
		return powerstandards;
	}

	public void setPowerstandards(String powerstandards) {
		this.powerstandards = powerstandards;
	}

	

}
