package com.scube.chargingstation.service;

import java.util.List;

import javax.validation.Valid;

import com.scube.chargingstation.dto.CheckAllChargerStatusDto;
import com.scube.chargingstation.dto.incoming.ServerAPIResetIncomingDto;

public interface ServerAPIResetService {
	
	boolean hardReset(@Valid ServerAPIResetIncomingDto serverAPIResetIncomingDto);
	
	boolean softReset(@Valid ServerAPIResetIncomingDto serverAPIResetIncomingDto);
	
	boolean sendEmailForChargerStatus();
	
	boolean saveAllChargerStatusData();
	
	List<CheckAllChargerStatusDto> getAllChargerStatusDataReportByDateRange(@Valid ServerAPIResetIncomingDto serverAPIResetIncomingDto);

}
