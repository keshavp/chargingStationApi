package com.scube.chargingstation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scube.chargingstation.dto.AdminDashboardDto;
import com.scube.chargingstation.dto.MostActiveChargingStationsDto;
import com.scube.chargingstation.dto.RecentRehargeDto;
import com.scube.chargingstation.dto.UserCarRespDto;
import com.scube.chargingstation.dto.UserDashboardDto;
import com.scube.chargingstation.dto.mapper.AverageSessionMapper;
import com.scube.chargingstation.entity.ChargingRequestEntity;
import com.scube.chargingstation.entity.UserInfoEntity;
import com.scube.chargingstation.exception.BRSException;
import com.scube.chargingstation.repository.UserInfoRepository;

@Service
public class DashboardServiceImpl implements DashboardService {

	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	ChargingRequestService chargingRequestService;
	
	@Autowired
	UserInfoRepository userInfoRepository;
	
	@Autowired
	UserCarService userCarService;
	
	
	public AdminDashboardDto getAdminDashboard() {
		// TODO Auto-generated method stub
		
		int weekaddedUserCount =  userInfoService.findCountForWeekNewAddedUser();
		int yesterdayConsumedKwh = chargingRequestService.getYesterdayConsumedKwh();
		int weekConsumedKwh = chargingRequestService.getWeekConsumedKwh();
		String MonthdaysTotalChargingTime = chargingRequestService.get30daysTotalChargingTime(); 
		int weekTotalChargingRequestCountSessions  = chargingRequestService.weekTotalChargingRequestCountSessions();
		List<MostActiveChargingStationsDto> getMostActiveChargingStations = chargingRequestService.getMostActiveChargingStations();
		
		return  new AdminDashboardDto()
				.setYesterdayConsumedKwh(yesterdayConsumedKwh)
				.setWeekNewUserCount(weekaddedUserCount)
        		.setMostActiveChargingStations(getMostActiveChargingStations)
        		.setAverageSession(AverageSessionMapper.toAverageSessionDto("Total Time",MonthdaysTotalChargingTime))        		
        		.setWeekConsumedKwh(weekConsumedKwh)
        		.setWeekSessionscount(weekTotalChargingRequestCountSessions);

	}

	public AdminDashboardDto getPartnerDashboardById(String mobileNumber) {
		// TODO Auto-generated method stub
		
		UserInfoEntity userInfoEntity = userInfoRepository.findByMobilenumber(mobileNumber);
		if(userInfoEntity==null)
		{
			throw BRSException.throwException("Error: User does not exist"); 
		}
		
		if(!userInfoEntity.getRole().getNameCode().equals("PU"))
		{
			throw BRSException.throwException("Error: User does not exist"); 
		}
		
//		int weekaddedUserCount =  userInfoService.findCountForWeekNewAddedUserByPartnerId();
		int yesterdayConsumedKwh = chargingRequestService.getYesterdayConsumedKwhByPartnerId(userInfoEntity);
		int weekConsumedKwh = chargingRequestService.getWeekConsumedKwhByPartnerId(userInfoEntity);
		String MonthdaysTotalChargingTime = chargingRequestService.get30daysTotalChargingTimeByPartnerId(userInfoEntity); 
		int weekTotalChargingRequestCountSessions  = chargingRequestService.weekTotalChargingRequestCountSessionsByPartnerId(userInfoEntity);
		List<MostActiveChargingStationsDto> getMostActiveChargingStations = chargingRequestService.getMostActiveChargingStationsByPartnerId(userInfoEntity);
		
		return  new AdminDashboardDto()
				.setYesterdayConsumedKwh(yesterdayConsumedKwh)
//				.setWeekNewUserCount(weekaddedUserCount)
        		.setMostActiveChargingStations(getMostActiveChargingStations)
        		.setAverageSession(AverageSessionMapper.toAverageSessionDto("Total Time",MonthdaysTotalChargingTime))        		
        		.setWeekConsumedKwh(weekConsumedKwh)
        		.setWeekSessionscount(weekTotalChargingRequestCountSessions);

	}

	public UserDashboardDto getUserDashboardByMobileNumber(String mobileNumber) {
		// TODO Auto-generated method stub
		
		UserInfoEntity userInfoEntity = userInfoRepository.findByMobilenumber(mobileNumber);
		if(userInfoEntity==null)
		{
			throw BRSException.throwException("Error: User does not exist"); 
		}
		
		UserDashboardDto userDashboardDto = chargingRequestService.getUserChargingRequestDetails(userInfoEntity.getId());
		
		ChargingRequestEntity chargingRequestEntity  = chargingRequestService.getRecentReharge(userInfoEntity.getId()) ;
		
		
		return new UserDashboardDto()
				.setWalletBalance(userDashboardDto.getWalletBalance())
				.setTotalRecharge(userDashboardDto.getTotalRecharge())
				.setTotalAmountSpent(userDashboardDto.getTotalAmountSpent())
				.setRecentReharge(new RecentRehargeDto()
						.setRechargeDate(chargingRequestEntity.getStartTime())
						.setRechargePlace(chargingRequestEntity.getChargingPointEntity().getName())
						.setRechargeAmount(chargingRequestEntity.getFinalAmount())
						);
	}

	@Override
	public List<UserCarRespDto> getUserProfileByMobileNumber(String mobilenumber) {
		// TODO Auto-generated method stub
		return userCarService.getUserCars(mobilenumber);
	}

}
