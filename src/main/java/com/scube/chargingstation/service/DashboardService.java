package com.scube.chargingstation.service;

import com.scube.chargingstation.dto.AdminDashboardDto;
import com.scube.chargingstation.dto.UserDashboardDto;

public interface DashboardService {

	Object getPartnerDashboardById(String id);

	UserDashboardDto getUserDashboardByMobileNumber(String id);

	AdminDashboardDto getAdminDashboard();

}
