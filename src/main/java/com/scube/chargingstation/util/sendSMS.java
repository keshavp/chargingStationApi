/*
 * package com.scube.chargingstation.util; import java.io.BufferedReader; import
 * java.io.InputStreamReader; import java.io.OutputStreamWriter; import
 * java.net.HttpURLConnection; import java.net.URL; import
 * java.net.URLConnection; import java.net.URLEncoder;
 * 
 * public class sendSMS { public String sendSms() { try { // Construct data
 * String apiKey = "apikey=" + "NzI3MzYyNDUzNTYyMzY3NzYyNTY2OTZhNDI0MzRiMzA=";
 * String message = "&message=" + "This is your message"; // String sender =
 * "&sender=" + "TXTLCL"; String numbers = "&numbers=" + "919892211829"; //
 * 919892211829 System.out.println("=====start======="); // Send data
 * HttpURLConnection conn = (HttpURLConnection) new
 * URL("https://api.textlocal.in/send/?").openConnection(); String data = apiKey
 * + numbers + message ; conn.setDoOutput(true); conn.setRequestMethod("POST");
 * conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
 * conn.getOutputStream().write(data.getBytes("UTF-8")); final BufferedReader rd
 * = new BufferedReader(new InputStreamReader(conn.getInputStream())); final
 * StringBuffer stringBuffer = new StringBuffer(); String line; while ((line =
 * rd.readLine()) != null) { stringBuffer.append(line); } rd.close();
 * 
 * System.out.println("=====End======="+stringBuffer.toString());
 * 
 * return stringBuffer.toString(); } catch (Exception e) {
 * System.out.println("Error SMS "+e); return "Error "+e; } } }
 */