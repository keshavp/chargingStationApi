package com.scube.chargingstation.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class InstantToString {

	
	
	public static String formatInstantToStringTimeZone(Instant instant) { 
	
	DateTimeFormatter formatter =
		    DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT )
		                     .withLocale( Locale.UK )
		                     .withZone( ZoneId.systemDefault() );
		return formatter.format(instant);
	
	}
}
