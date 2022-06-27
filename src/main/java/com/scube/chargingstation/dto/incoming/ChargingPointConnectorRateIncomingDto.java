package com.scube.chargingstation.dto.incoming;

import java.util.ArrayList;
import java.util.List;

import com.scube.chargingstation.entity.ChargingPointEntity;
import com.scube.chargingstation.entity.ConnectorEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

public class ChargingPointConnectorRateIncomingDto {

	private  List<PriceMasterDto> amount ;
	private String chargingPointId;
	private String connectorId;
	private String id;
}
