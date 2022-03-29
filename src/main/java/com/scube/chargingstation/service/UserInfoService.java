package com.scube.chargingstation.service;

import javax.validation.Valid;

import com.scube.chargingstation.dto.incoming.UserInfoIncomingDto;

public interface UserInfoService {

	boolean addUserInfo(@Valid UserInfoIncomingDto userInfoIncomingDto);

}
