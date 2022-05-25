package com.scube.chargingstation.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "connectorstatus")
@Getter @Setter @ToString 
public class ConnectorStatusEntity {

	@Id
	@GeneratedValue(generator = "custom-generator", strategy = GenerationType.IDENTITY)
	@GenericGenerator(
			name = "custom-generator",
			strategy = "com.scube.chargingstation.model.id.generator.BaseIdentifierGenerator")
	private String id;

	private String  ChargePointId;
	private int  ConnectorId;
	private String  ConnectorName;
	private String  LastStatus;
	private Date LastStatusTime;
	private double LastMeter;
	private Date LastMeterTime;
	private double ChargeSpeed;
	private double SoC;
	private int  TransactionId;
	
}
