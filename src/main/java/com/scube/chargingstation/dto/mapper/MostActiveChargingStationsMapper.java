package com.scube.chargingstation.dto.mapper;

import java.util.ArrayList;
import java.util.List;

import com.scube.chargingstation.dto.MostActiveChargingStationsDto;

public class MostActiveChargingStationsMapper {

	public static MostActiveChargingStationsDto toMostActiveChargingStationsDto(String name , String kwh) {
 		
        return new MostActiveChargingStationsDto()
        		.setKwh(kwh)
        		.setName(name);
	}

	public static List<MostActiveChargingStationsDto> toMostActiveChargingStationsDto() {
		// TODO Auto-generated method stub
		List<MostActiveChargingStationsDto>	activeChargingStationsDtos = new ArrayList<MostActiveChargingStationsDto>();
		
			activeChargingStationsDtos.add(toMostActiveChargingStationsDto("Nitin","23"));
			activeChargingStationsDtos.add(toMostActiveChargingStationsDto("Kapur","0"));
			activeChargingStationsDtos.add(toMostActiveChargingStationsDto("TACW2242321G0295","0"));
			activeChargingStationsDtos.add(toMostActiveChargingStationsDto("TACW2242321G0275","0"));
		
		return activeChargingStationsDtos;
	}
}
