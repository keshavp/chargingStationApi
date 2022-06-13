package com.scube.chargingstation.service;

import java.util.List;

import com.scube.chargingstation.dto.AdminDashboardDto;
import com.scube.chargingstation.dto.UserCarRespDto;
import com.scube.chargingstation.dto.UserDashboardDto;

public interface DashboardService {

	Object getPartnerDashboardById(String id);

	UserDashboardDto getUserDashboardByMobileNumber(String id);

	AdminDashboardDto getAdminDashboard();

	List<UserCarRespDto> getUserProfileByMobileNumber(String mobilenumber);

}
