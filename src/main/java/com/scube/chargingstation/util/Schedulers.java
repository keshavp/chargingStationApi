package com.scube.chargingstation.util;

import java.io.File;
import java.nio.file.Files;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import com.scube.chargingstation.service.ChargingRequestService;
import com.scube.chargingstation.service.TransactionsService;
import com.scube.chargingstation.service.UserInfoOtpService;

@Component
public class Schedulers {

	private static final Logger log = LoggerFactory.getLogger(Schedulers.class);
	
	
	@Autowired
	TransactionsService	transactionsService;	
	
	@Autowired 
	ChargingRequestService chargingRequestService;
	   
	@Autowired
	UserInfoOtpService	infoOtpService; 
	
	
	// 5
	@Scheduled(cron = "${updateStartResult.cronTime}")
	public int updateStartResult() throws Exception {
		    
		//log.info("updateStartResult");
		
	 	// transactionsService.updateStartResultInitiated();
	 	transactionsService.chargingRequestedBill();
	 	
	 	infoOtpService.removeUnVerificationUser();
	 	
		return 0;
	}
	
	/*
	 * @Scheduled(cron = "${timeoutPendingChargingRequests.cronTime}") public int
	 * timeoutPendingChargingRequests() throws Exception {
	 * 
	 * log.info("timeoutPendingChargingRequests");
	 * chargingRequestService.timeoutPendingChargingRequests(); return 0; }
	 */
	
	@Scheduled(cron = "${sendGunInsertNotification.cronTime}")
	public int sendGunInsertNotification() throws Exception {
		
		//log.info("sendGunInsertNotification");
		chargingRequestService.sendGunInsertNotification();
		return 0;
	}
	
	
	@Scheduled(cron = "${stopChargingOnTime.cronTime}")
	public int stopChargingOnTime() throws Exception {
		
		//log.info("sendGunInsertNotification");
		chargingRequestService.stopChargingOnTime();
		return 0;
	}
}
