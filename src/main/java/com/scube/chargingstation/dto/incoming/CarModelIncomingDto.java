package com.scube.chargingstation.dto.incoming;

import java.util.Set;

import com.scube.chargingstation.dto.ChargerTypeDto;
import com.scube.chargingstation.entity.ChargerTypeEntity;
import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString

public class CarModelIncomingDto {
	
	private String model;
	private String description;
	private String  status ;
	private String  imgPath;
    private Set<ChargerTypeDto> chargertypes;
    private String id;
	

}
