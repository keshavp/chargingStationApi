package com.scube.chargingstation.service;

import java.util.List;

import com.scube.chargingstation.dto.CarModelDto;
import com.scube.chargingstation.dto.incoming.NotificationReqDto;
import com.scube.chargingstation.dto.incoming.UserWalletRequestDto;
import com.scube.chargingstation.entity.CarModelEntity;
import com.scube.chargingstation.entity.ChargingPointEntity;

public interface CarModelService {

	
	List<CarModelDto> getCarModels();
	
	

}
