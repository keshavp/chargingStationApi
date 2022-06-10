package com.scube.chargingstation.service;

import com.scube.chargingstation.dto.AdminDashboardDto;

public interface DashboardService {

	Object getPartnerDashboardById(String id);

	Object getUserDashboardById(String id);

	AdminDashboardDto getAdminDashboard();

}
