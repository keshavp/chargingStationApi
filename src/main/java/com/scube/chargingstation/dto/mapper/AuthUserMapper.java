package com.scube.chargingstation.dto.mapper;

import com.scube.chargingstation.dto.AuthUserDto;
import com.scube.chargingstation.entity.UserInfoEntity;

public class AuthUserMapper {

	
	public static AuthUserDto toUserLoginDto(UserInfoEntity userInfo,String jwt,String refreshToken) {
		 		
        return new AuthUserDto()
        		//.setUserid(userInfo.getId())
        		.setUsername(userInfo.getUsername())
        		.setRole(userInfo.getRole().getNameCode())
        		.setRefreshToken(refreshToken)
        		.setTokenType("Bearer")
        		.setAccessToken(jwt);
    }
}
