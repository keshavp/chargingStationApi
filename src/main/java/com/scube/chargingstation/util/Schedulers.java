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

import com.scube.chargingstation.service.TransactionsService;

@Component
public class Schedulers {

	private static final Logger log = LoggerFactory.getLogger(Schedulers.class);
	
	
	@Autowired
	TransactionsService	transactionsService;	
	
	@Scheduled(cron = "${updateStartResult.cronTime}")
	public int updateStartResult() throws Exception {
		
		log.info("updateStartResult");
		
	 	transactionsService.updateStartResultInitiated();
	 	
	 	
		return 0;
	}
}
