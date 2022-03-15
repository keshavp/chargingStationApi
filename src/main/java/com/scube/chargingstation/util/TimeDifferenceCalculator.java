package com.scube.chargingstation.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TimeDifferenceCalculator {
	
	private static final Logger logger = LoggerFactory.getLogger(TimeDifferenceCalculator.class);
	
	public static String calculateDifference(String time1, String time2) {
		// to find difference between time which are in yyyy-mm-dd HH:mm:ss format
		logger.info("****TimeDifferenceCalculator calculateDifference****" + time1 + "   " + time2);
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		String timeDiff = "";
		
		try {
			Date bigTime = simpleDateFormat.parse(time1);
			Date smallTime = simpleDateFormat.parse(time2);
			
			long differenceInMilliSeconds = Math.abs(bigTime.getTime() - smallTime.getTime());
			long differenceInHours = (differenceInMilliSeconds / (60 * 60 * 1000)) % 24;
			long differenceInMinutes = (differenceInMilliSeconds / (60 * 1000)) % 60;
			long differenceInSeconds = (differenceInMilliSeconds / 1000) % 60;
			timeDiff = String.valueOf(differenceInHours)+ ":" + String.valueOf(differenceInMinutes) + ":" + String.valueOf(differenceInSeconds);
			logger.info("diff-->"+ differenceInHours+":"+differenceInMinutes+":"+differenceInSeconds);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return timeDiff;
		
	}
	
public static String calculateDiffBetweenTimeStrings(String time1, String time2) {
		// to find difference between time which are in HH:mm:ss format
		logger.info("****TimeDifferenceCalculator calculateDifference****" + time1 + "   " + time2);
		
		String timeDiff = "";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
		
		Date date1;
		try {
			date1 = simpleDateFormat.parse(time1);
			Date date2 = simpleDateFormat.parse(time2);
			
			long differenceInMilliSeconds = Math.abs(date1.getTime() - date2.getTime());
			long differenceInHours = (differenceInMilliSeconds / (60 * 60 * 1000)) % 24;
			long differenceInMinutes = (differenceInMilliSeconds / (60 * 1000)) % 60;
			long differenceInSeconds = (differenceInMilliSeconds / 1000) % 60;
			timeDiff = String.valueOf(differenceInHours)+ ":" + String.valueOf(differenceInMinutes) + ":" + String.valueOf(differenceInSeconds);
			logger.info("diff-->"+ differenceInHours+":"+differenceInMinutes+":"+differenceInSeconds);
			return timeDiff;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return timeDiff;
		
//		int index1hrs = time1.indexOf(":");
//		Long hours1 = Long.valueOf(time1.substring(0,index1hrs));
//		int index1mins = time1.indexOf(":", time1.indexOf(":") + 1);
//		Long mins1 = Long.valueOf(time1.substring(index1hrs+1, index1mins));
//		Long secs1 = Long.valueOf(time1.substring(index1mins + 1, time1.length()));
//		
//		int index2hrs = time2.indexOf(":");
//		Long hours2 = Long.valueOf(time2.substring(0,index2hrs));
//		int index2mins = time2.indexOf(":", time2.indexOf(":") + 1);
//		Long mins2 = Long.valueOf(time2.substring(index2hrs+1, index2mins));
//		Long secs2 = Long.valueOf(time2.substring(index2mins + 1, time2.length()));
//		
//		Long totalHrs = Math.abs(hours1 - hours2);
//		Long totalMins = Math.abs(mins1 - mins2);
//		Long totalSecs = Math.abs(secs1 - secs2);
//		
//		logger.info("total-->"+ String.valueOf(totalHrs) + ":" + String.valueOf(totalMins) + ":" + String.valueOf(totalSecs));
//		timeDiff = String.valueOf(totalHrs) + ":" + String.valueOf(totalMins) + ":" + String.valueOf(totalSecs);
//		return timeDiff;
		
	}

}
