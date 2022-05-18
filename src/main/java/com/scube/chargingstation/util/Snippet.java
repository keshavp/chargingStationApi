package com.scube.chargingstation.util;

import java.time.Instant;
import java.util.Date;

public class Snippet {
	public static String twoDateDifference(Date startDate, Date endDate){
	
	    //milliseconds
	    long different = endDate.getTime() - startDate.getTime();
	
	    System.out.println("startDate : " + startDate);
	    System.out.println("endDate : "+ endDate);
	    System.out.println("different : " + different);
	
	    long secondsInMilli = 1000;
	    long minutesInMilli = secondsInMilli * 60;
	    long hoursInMilli = minutesInMilli * 60;
	    long daysInMilli = hoursInMilli * 24;
	
	    //long elapsedDays = different / daysInMilli;
	    //different = different % daysInMilli;
	
	    long elapsedHours = different / hoursInMilli;
	    different = different % hoursInMilli;
	
	    long elapsedMinutes = different / minutesInMilli;
	    different = different % minutesInMilli;
	
	    long elapsedSeconds = different / secondsInMilli;
	
	    System.out.printf("%d hours, %d minutes, %d seconds%n",elapsedHours, elapsedMinutes, elapsedSeconds);
	    
	   return String.format("%02d", elapsedHours) +":"+String.format("%02d", elapsedMinutes)+":"+String.format("%02d", elapsedSeconds);
	
	}
	
	public static String twoInstantDifference(Instant instant, Instant instant2){
	
		
		long allSeconds = instant2.getEpochSecond() - instant.getEpochSecond();
	
	    System.out.println("startDate : " + instant);
	    System.out.println("endDate : "+ instant2);
	    System.out.println("different : " + allSeconds);
	
	    long seconds = allSeconds % 60;
	    long minutes = allSeconds / 60 % 60; // minutes 
	    long hours = allSeconds / 3600 % 24; // hours
	    long days = allSeconds / 86400; // days 
	
	
	    System.out.printf("%d hours, %d minutes, %d seconds%n",hours, minutes, seconds);
	    
	   return String.format("%02d", hours) +":"+String.format("%02d", minutes)+":"+String.format("%02d", seconds);
	
	}
}

