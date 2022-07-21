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

import com.scube.chargingstation.service.BookingRequestService;
import com.scube.chargingstation.service.ChargingRequestService;
import com.scube.chargingstation.service.PartnerService;
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
	
	@Autowired
	BookingRequestService bookingRequestService;
	
	@Autowired
	PartnerService partnerService;
	
	// 5
	@Scheduled(cron = "${updateStartResult.cronTime}")
	public int updateStartResult() throws Exception {
		    
		//log.info("updateStartResult ======================== "+ new Date().getTime());
	 	// transactionsService.updateStartResultInitiated();
	 	transactionsService.chargingRequestedBill();
	 	
	 	infoOtpService.removeUnVerificationUser();
	 	
		return 0;
	}
	
	@Scheduled(cron = "${bookingAuto.cronTime}")
	public void bookingAutoCancellation() throws Exception {

		//log.info("bookingAutoCancellation ======================== "+ new Date().getTime());
		
		bookingRequestService.bookingAutoCancellationSchedulers();
	}
	
	@Scheduled(cron = "${bookingReminder.oneDayBefore}")
	public int oneDayBeforeBookingReminder() throws Exception {
		    
		log.info("oneDayBeforeBookingReminderSchedulers ======================== "+ new Date().getTime());
		bookingRequestService.oneDayBeforeBookingReminderSchedulers();
	 	
		return 0;
	}
	
	@Scheduled(cron = "${bookingReminder.oneHour}")
	public int oneHourBeforeBookingReminder() throws Exception {
		    
		log.info("oneHourBeforeBookingReminderSchedulers ======================== "+ new Date().getTime());
		bookingRequestService.oneHourBeforeBookingReminderSchedulers();
	 	
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
	
	@Scheduled(cron = "${partnersDailyShare.cronTime}")
	public int insertPartnersDailyShare() throws Exception {
		
		//log.info("sendGunInsertNotification");
		partnerService.addPartnerDailyShare();
		return 0;
	}
}
