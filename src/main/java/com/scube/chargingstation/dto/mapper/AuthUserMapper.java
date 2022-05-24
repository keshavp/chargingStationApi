package com.scube.chargingstation.dto.mapper;

import java.util.ArrayList;
import java.util.List;

import com.scube.chargingstation.dto.AuthUserDto;
import com.scube.chargingstation.entity.UserInfoEntity;

public class AuthUserMapper {

	
	public static AuthUserDto toUserLoginDto(UserInfoEntity userInfo,String jwt,String refreshToken) {
		 		
        return new AuthUserDto()
        		.setUserId(userInfo.getId())
        		.setUsername(userInfo.getUsername())
        		.setRole(userInfo.getRole().getNameCode())
        		.setRefreshToken(refreshToken)
        		.setTokenType("Bearer")
        		.setAccessToken(jwt)
        		.setMobileno(userInfo.getMobilenumber());
    }	
	public static List<AuthUserDto> toUserLoginDto(List<UserInfoEntity> userInfoEntities) {
		// TODO Auto-generated method stub
		
		List<AuthUserDto> authUserDtos= new ArrayList<AuthUserDto>();
		for(UserInfoEntity userinfoEntity :userInfoEntities)
		{
			authUserDtos.add((AuthUserDto) toUserLoginDto(userinfoEntity, null, null));			
		}
				
		return authUserDtos;
	}
	
	public static AuthUserDto toUserLoginDto(UserInfoEntity userInfo) {
 		
        return new AuthUserDto()
        		.setUserId(userInfo.getId())
        		.setUsername(userInfo.getUsername())
        		.setRole(userInfo.getRole().getNameCode())  	
        		.setMobileno(userInfo.getMobilenumber());
    }
}
