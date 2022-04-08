package com.scube.chargingstation.dto.response;


import java.util.HashSet;
import java.util.Set;




import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter 
@Setter
@ToString
public class ChargingPointResponse {
	
	
	
	private String address;
	private String logitude;
	private String distance;
	private String latitude;
	private String name;
	private String rating;
	private String status;
	private String chargingPointId;
    private Set<ChargingPointConnectorVo> ctypeVo = new HashSet<>();


}
