package com.scube.chargingstation.util; 

  import java.io.BufferedReader; 
  import java.io.InputStreamReader; 
  import java.net.HttpURLConnection; 
  import java.net.URL; 
  
  public class sendSMS { 
  public String sendSms() { 
	  try { // Construct data
		  
			  String apiKey 	= "apikey="+"NGM0ZjMzNDY0OTY3NDE3MzZhNGQ1NjczNjIzODQzMzg=";
			  String message 	= "&message="+"Use the OTP Xto complete the registration with EV Dock.-EVDOCK"; // 
			  String sender 	= "&sender="+"EVDOCK"; 
			  String numbers 	= "&numbers="+"919892211829"; //  919892211829 
			  
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
  }
 