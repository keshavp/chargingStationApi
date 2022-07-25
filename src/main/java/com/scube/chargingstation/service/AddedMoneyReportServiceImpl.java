package com.scube.chargingstation.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scube.chargingstation.dto.AddedMoneyReportDto;
import com.scube.chargingstation.dto.incoming.AddedMoneyWalletRequestDto;
import com.scube.chargingstation.entity.UserWalletDtlEntity;
import com.scube.chargingstation.exception.BRSException;
import com.scube.chargingstation.repository.UserWalletDtlRepository;

@Service
public class AddedMoneyReportServiceImpl implements AddedMoneyReportService{


	private static final Logger logger = LoggerFactory.getLogger(BookingRequestServiceImpl.class);
	
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	UserWalletDtlRepository userWalletDtlRepository;
	
	@Override
	public List<AddedMoneyReportDto> getAddedMoneyToWalletDetailsByUserId(AddedMoneyWalletRequestDto addedMoneyWalletRequestDto) {
		// TODO Auto-generated method stub
		
		logger.info("***AddedMoneyReportServiceImpl getAddedMoneyToWalletDetailsByUserId***");
		
		logger.info("Start Date : " + addedMoneyWalletRequestDto.getStartDate() + "End Date : " + addedMoneyWalletRequestDto.getEndDate());
		
		if(addedMoneyWalletRequestDto.getStartDate() ==  null && addedMoneyWalletRequestDto.getEndDate() == null) {
			
			throw BRSException.throwException("Error : Please select your Date Range");
			
		}
		
		String endInputDate = addedMoneyWalletRequestDto.getEndDate();
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		Calendar calendar = new GregorianCalendar();
		
			try {
				
				calendar.setTime(simpleDateFormat.parse(endInputDate));
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		calendar.add(Calendar.DATE, 1);
		
		String addOneDayToEndInputDate = simpleDateFormat.format(calendar.getTime());
		
		System.out.println("End input Date :---->>> " + addOneDayToEndInputDate);
		
		List<UserWalletDtlEntity> walletEntityList = userWalletDtlRepository.getAddedMoneyRecordsForAllUsers(addedMoneyWalletRequestDto.getStartDate(), addOneDayToEndInputDate);
		
		if(walletEntityList == null) {
			
			throw BRSException.throwException("Error : No Records Present");
			
		}
		
		logger.info("Wallet Entity List : " + walletEntityList);
		
		List<AddedMoneyReportDto> addedMoneyReportDtos = new ArrayList<>();
		
		for (UserWalletDtlEntity userWalletDtlEntity : walletEntityList) {
			
			AddedMoneyReportDto addedMoneyDto = new AddedMoneyReportDto();
			
			SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss aa");
			
			Instant userAddedMoneyTime = userWalletDtlEntity.getCreatedAt();
			Date userAddeddMoneyDate = Date.from(userAddedMoneyTime);
			
			String convertAddedMoneyDateToString = dateTimeFormatter.format(userAddeddMoneyDate);
			
			addedMoneyDto.setUserName(userWalletDtlEntity.getUserInfoEntity().getUsername());
			addedMoneyDto.setUserMobileNo(userWalletDtlEntity.getUserInfoEntity().getMobilenumber());
			addedMoneyDto.setAddedAmount(userWalletDtlEntity.getAmount());
			addedMoneyDto.setPaymentDate(convertAddedMoneyDateToString);
			
			addedMoneyReportDtos.add(addedMoneyDto);
			
		}
		
		return addedMoneyReportDtos;
	}

}
