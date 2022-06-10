package com.scube.chargingstation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scube.chargingstation.dto.AdminDashboardDto;
import com.scube.chargingstation.dto.MostActiveChargingStationsDto;
import com.scube.chargingstation.dto.mapper.AverageSessionMapper;

@Service
public class DashboardServiceImpl implements DashboardService {

	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	ChargingRequestService chargingRequestService;
	
	
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

	public Object getPartnerDashboardById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getUserDashboardById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

}
