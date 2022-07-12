package com.scube.chargingstation.dto.incoming;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ConnectorTypeIncomingDto {
	
	private String name;
	private String imagePath;
	private String status;
    private String id;
    
    private String slotInterval;
}
