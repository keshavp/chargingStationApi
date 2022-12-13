package com.scube.chargingstation.util;

public class StaticPathContUtils {

	// Sarvesh Local
//		public static final String APP_URL_DIR = "http://192.168.0.210:8081/chargingStationApi/api/v1/";
	
	// internal server		
	//	public static final String APP_URL_DIR = "http://125.99.153.126:8085/chargingStationApi/api/v1/";
	//	public static final String SERVER_API_URL = "http://125.99.153.126:8080/API/";

		// aws server		
//		public static final String APP_URL_DIR = "https://evdock.app/chargingStationApi/api/v1/";
		public static final String SERVER_API_URL = "http://evdock.app:8082/API/";
		
		public static final String SET_RECEIPT_FILE_URL_DIR = "images/getDoc/RC/";
		public static final String SET_BOOKING_RECEIPT_FILE_URL_DIR = "images/getDoc/BRC/";
		public static final String SET_CHARGER_TYPE_FILE_URL_DIR = "images/getImage/CT/";
		public static final String SET_CAR_MODEL_TYPE_FILE_URL_DIR = "images/getImage/CMT/";
		   
		//AWS
		public static final String CCAVENUE_CANCELURL = "https://evdock.app/chargingStation/cancelUrl";
		public static final String CCAVENUE_REDIRECTURL = "https://evdock.app/chargingStation/redirectUrl";    

		//internal server
	//	public static final String CCAVENUE_CANCELURL = "http://125.99.153.126:8085/chargingStation/cancelUrl";
	//	public static final String CCAVENUE_REDIRECTURL = "http://125.99.153.126:8085/chargingStation/redirectUrl";
	//	public static final String CCAVENUE_REDIRECT_API_URL = "http://125.99.153.126:8085/chargingStationApi/api/v1/userpayment/goToRedirectUrl";
		
		//localhost
	//	public static final String CCAVENUE_CANCELURL = "http://localhost:8080/chargingStation/cancelUrl";
	//	public static final String CCAVENUE_REDIRECTURL = "http://localhost:8080/chargingStation/redirectUrl";
			
}
