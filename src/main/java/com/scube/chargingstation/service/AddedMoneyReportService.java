package com.scube.chargingstation.service;

import java.util.List;

import com.scube.chargingstation.dto.AddedMoneyReportDto;
import com.scube.chargingstation.dto.incoming.AddedMoneyWalletRequestDto;


public interface AddedMoneyReportService {
	
	List<AddedMoneyReportDto> getAddedMoneyToWalletDetailsByUserId(AddedMoneyWalletRequestDto addedMoneyWalletRequestDto);

}
