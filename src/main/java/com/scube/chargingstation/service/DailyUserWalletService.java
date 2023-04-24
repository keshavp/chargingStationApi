package com.scube.chargingstation.service;

import java.util.List;

import com.scube.chargingstation.dto.DailyUserWalletReportDto;
import com.scube.chargingstation.dto.incoming.DailyUserWalletreportIncomingDto;
import com.scube.chargingstation.entity.DailyUserWalletEntity;

public interface DailyUserWalletService {

	boolean getDailyUserWalletBalance();
	
	List<DailyUserWalletReportDto> getDailyEmpWallets(DailyUserWalletreportIncomingDto dailyUserWalletreportIncomingDto);
	
}


