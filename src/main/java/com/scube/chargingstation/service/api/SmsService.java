package com.scube.chargingstation.service.api; 
  import java.io.BufferedReader;
  import java.io.DataOutputStream; 
  import java.io.InputStreamReader; 
  import java.net.HttpURLConnection; 
  import java.net.URL; 
  import java.net.URLEncoder;
  import java.util.Random;
  
  import org.slf4j.Logger; 
  import org.slf4j.LoggerFactory; 
  import org.springframework.stereotype.Service;
  
  @Service 
  public class SmsService { 
	  private static final Logger logger = LoggerFactory.getLogger(SmsService.class);
  
  static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:52.0) Gecko/20100101 Firefox/52.0";
  
  public int generateRandomMobileOTP(){
  
  Random rand = new Random(); 
  int resRandom = rand.nextInt((9999 - 100) + 1) + 10; 
  logger.info("Random OTP for Mobile verification: "+resRandom); 
  return resRandom;
  
  }
  
  
  public static String SendSMS(String senderId ,String messageBody,String sendNumbers){ 
	  try { // Construct data
		  
		  String apiKey 	= "apikey="+"NGM0ZjMzNDY0OTY3NDE3MzZhNGQ1NjczNjIzODQzMzg=";
		  String message 	= "&message="+messageBody; //   "Use the OTP Xto complete the registration with EV Dock.-EVDOCK"; // 
		  String sender 	= "&sender="+senderId; // "EVDOCK"; 
		  String numbers 	= "&numbers="+sendNumbers; //  919892211829 
		  
		// Send data
			HttpURLConnection conn = (HttpURLConnection) new URL("https://api.textlocal.in/send/?").openConnection();
			String data = apiKey + numbers + message + sender;
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
			conn.getOutputStream().write(data.getBytes("UTF-8"));
			final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			final StringBuffer stringBuffer = new StringBuffer();
			String line;
			while ((line = rd.readLine()) != null) {
				stringBuffer.append(line);
			}
			rd.close();
			
			System.out.println(stringBuffer.toString());
			
			return stringBuffer.toString();
			
		  } catch (Exception e) {
			  
		  System.out.println("Error SMS "+e); 
		  return "Error "+e; 
	  } 
  }
  
  public void sendSignupOTPMobile(String verifymobiletokenString, String mobilenumber) {
  
  String senderId="EVDOCK";
  String mobileNo=mobilenumber; 
  String SMSText="Use the OTP Xto complete the registration with EV Dock.-EVDOCK";
  
  String resp =SendSMS(senderId ,SMSText,mobileNo);
  
  logger.info("Send SMS Response :[ "+ resp+"]");
  
  } }
 