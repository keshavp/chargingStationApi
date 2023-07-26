package com.scube.chargingstation.dto.mapper;

import java.util.ArrayList;
import java.util.List;

import com.scube.chargingstation.dto.DailyUserWalletReportDto;
import com.scube.chargingstation.entity.DailyUserWalletEntity;
import com.scube.chargingstation.util.StringNullEmpty;

public class DailyUserWalletMapper {
	
public static List<DailyUserWalletReportDto> toDailyUserWalletReportDto(List<DailyUserWalletEntity> dailyUserWalletEntities) {
		
	List<DailyUserWalletReportDto> chaReportDtos = new ArrayList<DailyUserWalletReportDto>();
	
	for(DailyUserWalletEntity dailyUserWalletEntity : dailyUserWalletEntities) {
		chaReportDtos.add(toDailyUserWalletRepDto(dailyUserWalletEntity));
	}
	return chaReportDtos;
      
}

public static DailyUserWalletReportDto toDailyUserWalletRepDto(DailyUserWalletEntity dailyUserWalletEntity) { 
	
	return new DailyUserWalletReportDto()
	.setName(StringNullEmpty.stringNullAndEmptyToBlank(dailyUserWalletEntity.getUserInfoEntity() != null ?
			dailyUserWalletEntity.getUserInfoEntity().getUsername() : ""))
	.setMobileno(StringNullEmpty.stringNullAndEmptyToBlank(dailyUserWalletEntity.getUserInfoEntity() != null ?
			dailyUserWalletEntity.getUserInfoEntity().getMobilenumber() : ""))
	.setWalletBalance(dailyUserWalletEntity.getCurrentBalance())
	.setLatestDate(dailyUserWalletEntity.getLatest_Date().trim());
}

}
