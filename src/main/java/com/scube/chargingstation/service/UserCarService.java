package com.scube.chargingstation.service;

import java.util.List;

import javax.validation.Valid;

import com.scube.chargingstation.dto.UserCarRespDto;
import com.scube.chargingstation.dto.incoming.UserCarDto;
import com.scube.chargingstation.entity.UserInfoEntity;

public interface UserCarService {

	
	public boolean addUserCars(UserCarDto userCarDto);
	

	public List<UserCarRespDto> getUserCars(String mobileUser_Id);


	public Object removeUserCar(@Valid UserCarDto userCarDto);

}
