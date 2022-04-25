/*
 * package com.scube.chargingstation.service.api; import java.io.BufferedReader;
 * import java.io.DataOutputStream; import java.io.InputStreamReader; import
 * java.net.HttpURLConnection; import java.net.URL; import java.net.URLEncoder;
 * import java.util.Random;
 * 
 * import org.slf4j.Logger; import org.slf4j.LoggerFactory; import
 * org.springframework.stereotype.Service;
 * 
 * @Service public class SmsService { private static final Logger logger =
 * LoggerFactory.getLogger(SmsService.class);
 * 
 * static final String USER_AGENT =
 * "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:52.0) Gecko/20100101 Firefox/52.0";
 * 
 * public int generateRandomMobileOTP(){
 * 
 * Random rand = new Random(); int resRandom = rand.nextInt((9999 - 100) + 1) +
 * 10; logger.info("Random OTP for Mobile verification: "+resRandom); return
 * resRandom;
 * 
 * }
 * 
 * 
 * public static String SendSMS(String ID,String PWD, String MobileNos,String
 * text){ // Base URL String strUrl = "https://www.businesssms.co.in/smsaspx";
 * 
 * StringBuffer response = new StringBuffer(); try { URL obj = new URL(strUrl);
 * HttpURLConnection con = (HttpURLConnection) obj.openConnection(); //add
 * reuqest header con.setRequestMethod("POST");
 * con.setRequestProperty("User-Agent", USER_AGENT);
 * con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
 * con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
 * 
 * String templateId = "1007521316478068496";
 * 
 * String urlParameters = "ID="+ URLEncoder.encode(ID,
 * "UTF-8")+"&Pwd="+URLEncoder.encode(PWD,
 * "UTF-8")+"&PhNo="+URLEncoder.encode(MobileNos,
 * "UTF-8")+"&Text="+URLEncoder.encode(text,
 * "UTF-8")+"&TemplateID="+URLEncoder.encode(templateId, "UTF-8");
 * 
 * // Send post request con.setDoOutput(true); DataOutputStream wr = new
 * DataOutputStream(con.getOutputStream()); wr.writeBytes(urlParameters);
 * wr.flush(); wr.close();
 * 
 * int responseCode = con.getResponseCode();
 * logger.info("\nSending 'POST' request to URL : " + strUrl);
 * logger.info("Post parameters : " + urlParameters);
 * logger.info("Response Code : " + responseCode);
 * 
 * BufferedReader in = new BufferedReader( new
 * InputStreamReader(con.getInputStream())); String inputLine;
 * 
 * while ((inputLine = in.readLine()) != null) { response.append(inputLine); }
 * in.close();
 * 
 * }catch(Exception ex) { return ex.toString(); }
 * 
 * return response.toString(); }
 * 
 * public void sendSignupOTPMobile(String verifymobiletokenString, String
 * mobilenumber) {
 * 
 * String ID="hrushikeshjoshi@absortio.com"; String PWD="BookMyCargo@2021";
 * String MobileNos=mobilenumber; String
 * SMSText="Your OTP for mobile registration is "+verifymobiletokenString+".";
 * 
 * String resp =SendSMS(ID,PWD,MobileNos,SMSText);
 * 
 * logger.info("Send SMS Response :[ "+ resp+"]");
 * 
 * } }
 */