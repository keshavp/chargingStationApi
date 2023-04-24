package com.scube.chargingstation.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scube.chargingstation.dto.DailyUserWalletReportDto;
import com.scube.chargingstation.dto.incoming.DailyUserWalletreportIncomingDto;
import com.scube.chargingstation.dto.incoming.UserWalletRequestDto;
import com.scube.chargingstation.dto.mapper.DailyUserWalletMapper;
import com.scube.chargingstation.entity.DailyUserWalletEntity;
import com.scube.chargingstation.repository.DailyUserWalletRepository;

@Service
public class DailyUserWalletServiceImpl implements DailyUserWalletService{

	@Autowired
	DailyUserWalletRepository dailyUserWalletRepository;
	
	@Override
	public boolean getDailyUserWalletBalance() {
	    dailyUserWalletRepository.insertFromEmpWallet();
	    System.out.println("Inserted daily user wallet record.");
	    return true;
	}

	@Override
	public List<DailyUserWalletReportDto> getDailyEmpWallets(DailyUserWalletreportIncomingDto dailyUserWalletreportIncomingDto) {
		
		List<DailyUserWalletEntity> dailyUserWalletEntities = dailyUserWalletRepository.getLatestDate(dailyUserWalletreportIncomingDto.getLatestDate());
		 
		return DailyUserWalletMapper.toDailyUserWalletReportDto(dailyUserWalletEntities);
	}
	   
	


}

