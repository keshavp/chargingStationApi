package com.scube.chargingstation.service;

import static com.scube.chargingstation.exception.ExceptionType.ALREADY_EXIST;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.firebase.messaging.Notification;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.scube.chargingstation.controller.AuthController;
import com.scube.chargingstation.dto.ChargingPointDto;
import com.scube.chargingstation.dto.ChargingPointConnectorDto;
import com.scube.chargingstation.dto.incoming.ChargingRequestDto;
import com.scube.chargingstation.dto.incoming.ChargingStationDto;
import com.scube.chargingstation.dto.incoming.NotificationReqDto;
import com.scube.chargingstation.dto.incoming.UserWalletRequestDto;
import com.scube.chargingstation.dto.mapper.ChargingPointMapper;
import com.scube.chargingstation.dto.mapper.ConnectorMapper;
import com.scube.chargingstation.dto.response.Response;
import com.scube.chargingstation.entity.AmenitiesEntity;
import com.scube.chargingstation.entity.BookingRequestEntity;
import com.scube.chargingstation.entity.ChargerTypeEntity;
import com.scube.chargingstation.entity.ChargingPointEntity;
import com.scube.chargingstation.entity.ChargingRequestEntity;
import com.scube.chargingstation.entity.ConnectorEntity;
import com.scube.chargingstation.entity.UserInfoEntity;
import com.scube.chargingstation.entity.UserWalletDtlEntity;
import com.scube.chargingstation.entity.UserWalletEntity;
import com.scube.chargingstation.exception.BRSException;
import com.scube.chargingstation.exception.EntityType;
import com.scube.chargingstation.repository.ChargerTypeRepository;
import com.scube.chargingstation.repository.ChargingPointRepository;
import com.scube.chargingstation.repository.ChargingRequestRepository;
import com.scube.chargingstation.repository.UserInfoRepository;
import com.scube.chargingstation.repository.UserWalletDtlRepository;
import com.scube.chargingstation.repository.UserWalletRepository;

@Service
public class NotificationServiceImpl implements NotificationService {

	@Autowired
	ChargingPointRepository chargingPointRepository;
	
	@Autowired
	ChargingRequestRepository chargingRequestRepository;
	
	@Autowired
	ChargerTypeRepository chargerTypeRepository;
	
	@Autowired
	UserInfoRepository userInfoRepository;
	
	@Autowired  
	UserWalletRepository userWalletRepository;
	
	@Autowired
	UserWalletDtlRepository userWalletDtlRepository;
	
	@Autowired
	ChargingPointService	chargingPointService;
	
	@Autowired
	ConnectorService	connectorService;
	
  	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	

	
	@Override
	public boolean sendNotification(NotificationReqDto notificationReqDto) 
	{
		// String SERVER_KEY = "AAAAgHRHE7M:APA91bFC5F9IZ5_8KZhNROpIeKwVMrdjA_SM7ugvYjgeFwn81G-DRG9syVxHWbVg_198OVhEcBmHpZv_PLIuc4Xw3etQm8_0L-MBoQiVGRpIP0s0R1f5zr2ESZI0wbhPLFjo487zN-Po";
		
			UserInfoEntity userInfoEntity = userInfoRepository.findByMobilenumber(notificationReqDto.getMobileUser_Id());
			
			if(userInfoEntity==null) { 
				throw BRSException.throwException("Error: User does not exist"); 
			}
			
			
			String DEVICE_TOKEN =userInfoEntity.getFcmToken();
			
			//String DEVICE_TOKEN="dEpGWA5UROmaIvcq8OJtma:APA91bEdk7MRhaajWlub-3Ow4VUIE_upMvEX6W88Rc0AMJ4k9pAL-u8PwBeHudk67LlkW8wqos7yEQf1V33kqesgWbWzzD-vah0ez-H8_JNcayIKew5SFdzH2WRz92KdYC_BLHlTR_RR";
		            
			logger.info("Booking id - "+notificationReqDto.getSendid() + "DEVICE_TOKEN "+DEVICE_TOKEN +" for mobile no "+notificationReqDto.getMobileUser_Id());
			
			if((DEVICE_TOKEN==null)||(DEVICE_TOKEN.isEmpty()))
			{
			  throw BRSException.throwException("Error: NO Device Token");
			}
			
			
			String title="",body="";
			
			title=notificationReqDto.getTitle();
			body=notificationReqDto.getBody();
			
           // This registration token comes from the client FCM SDKs.
          //  String DEVICE_TOKEN = "f4aREZY8QGqbDNO2vTSq0X:APA91bEBhMdze1OVs26milHTNqs6hy-cMgrDD0O5IyqLci1q3ALTgyW4ODuuxBj-N0zg6l_bCaKup0hitvrB9MfFB8u5YsoLHMXsfUf__5x3--Qev4d5mr1Ro0KxN8gY7hMWKlGA3BZF";

            Message message = Message.builder()
            	    .setNotification(new Notification(title,body))
            	    .setToken(DEVICE_TOKEN)
            	    .build();

            // Send a message to the device corresponding to the provided
            // registration token.
            String response = null;
			try {
				response = FirebaseMessaging.getInstance().send(message);
			} catch (FirebaseMessagingException e) {
				// TODO Auto-generated catch block
				
				e.printStackTrace();
				  throw BRSException.throwException("Error: Notification not sent");

			}
            // Response is a message ID string.
            System.out.println("Successfully sent message: " + response);
          //  System.out.println("Successfully sent message: "+response.get
			 return true;
	}

	
	@Override
	public boolean sendBookingReminderNotification(BookingRequestEntity  bookingRequestEntity) 
	{
		// String SERVER_KEY = "AAAAgHRHE7M:APA91bFC5F9IZ5_8KZhNROpIeKwVMrdjA_SM7ugvYjgeFwn81G-DRG9syVxHWbVg_198OVhEcBmHpZv_PLIuc4Xw3etQm8_0L-MBoQiVGRpIP0s0R1f5zr2ESZI0wbhPLFjo487zN-Po";
		
			UserInfoEntity userInfoEntity = userInfoRepository.findByMobilenumber(bookingRequestEntity.getUserInfoEntity().getMobilenumber());
			
			if(userInfoEntity==null) { 
				throw BRSException.throwException("Error: User does not exist"); 
			}
			
			
			String DEVICE_TOKEN =userInfoEntity.getFcmToken();
			
			//String DEVICE_TOKEN="dEpGWA5UROmaIvcq8OJtma:APA91bEdk7MRhaajWlub-3Ow4VUIE_upMvEX6W88Rc0AMJ4k9pAL-u8PwBeHudk67LlkW8wqos7yEQf1V33kqesgWbWzzD-vah0ez-H8_JNcayIKew5SFdzH2WRz92KdYC_BLHlTR_RR";
		            
			logger.info("DEVICE_TOKEN "+DEVICE_TOKEN +" for mobile no "+bookingRequestEntity.getUserInfoEntity().getMobilenumber());
			
			if((DEVICE_TOKEN==null)||(DEVICE_TOKEN.isEmpty()))
			{
			  throw BRSException.throwException("Error: NO Device Token");
			}
			
			
			String title="",body="";
			
	//		title=notificationReqDto.getTitle();
	//		body=notificationReqDto.getBody();
			
           // This registration token comes from the client FCM SDKs.
          //  String DEVICE_TOKEN = "f4aREZY8QGqbDNO2vTSq0X:APA91bEBhMdze1OVs26milHTNqs6hy-cMgrDD0O5IyqLci1q3ALTgyW4ODuuxBj-N0zg6l_bCaKup0hitvrB9MfFB8u5YsoLHMXsfUf__5x3--Qev4d5mr1Ro0KxN8gY7hMWKlGA3BZF";

            Message message = Message.builder()
            	    .setNotification(new Notification(title,body))
            	    .setToken(DEVICE_TOKEN)
            	    .build();

            // Send a message to the device corresponding to the provided
            // registration token.
            String response = null;
			try {
				response = FirebaseMessaging.getInstance().send(message);
			} catch (FirebaseMessagingException e) {
				// TODO Auto-generated catch block
				
				e.printStackTrace();
				  throw BRSException.throwException("Error: Notification not sent");

			}
            // Response is a message ID string.
            System.out.println("Successfully sent message: " + response);
          //  System.out.println("Successfully sent message: "+response.get
			 return true;
	}
}


/*
 * boolean flag=false;
 * 
 * //String DEVICE_TOKEN =
 * "dwl8bXiIc6Q:APA91bHeq-5-Z6Ku7qXsLEnMsjDK4j13kXkxM_jfTMR2iIKIAPmrNmCOw9qdnSn9ttO18ticC4KSe7mAffOFeH7V2dbGpQYP37aIaioE__SVnOHLPJy7j5_SAImESV-mdH-Xv69W8ZkE";
 * 
 * String title = "CS Notification"; String message = "charging status";
 * 
 * 
 * 
 * try { // DEVICE_TOKEN=
 * "f_9giX9FSxygKuJtAAFcrE:APA91bFIIgzI20aoi6wI_xlsZ7LsWwob2UhwcqGTqeHywBHGqopYhhApMUUBgML9LSvIu6FIiiXt7RqF2E_HDMTbUwfjGGTsXyITqR4Qny0k73ujlVcv3_cZHCl2peplY9ptC8W3wVJJ";
 * 
 * String DEVICE_TOKEN=
 * "f4aREZY8QGqbDNO2vTSq0X:APA91bEBhMdze1OVs26milHTNqs6hy-cMgrDD0O5IyqLci1q3ALTgyW4ODuuxBj-N0zg6l_bCaKup0hitvrB9MfFB8u5YsoLHMXsfUf__5x3--Qev4d5mr1Ro0KxN8gY7hMWKlGA3BZF";
 * 
 * System.out.println("DEVICE_TOKEN--"+DEVICE_TOKEN);
 * 
 * Map<String, Object> jsonObjectData = new HashMap<String, Object>();
 * jsonObjectData.put("title", "titleljl"); jsonObjectData.put("body",
 * "testing notificationkkn"); //jsonObjectData.put("body",
 * "testing notification"); jsonObjectData.put("registration_ids",
 * DEVICE_TOKEN); jsonObjectData.put("to", DEVICE_TOKEN);
 * 
 * 
 * 
 * 
 * Map<String, Object> json = new HashMap<String, Object>(); json.put("to",
 * DEVICE_TOKEN); json.put("registration_id", DEVICE_TOKEN);
 * json.put("registration_ids", DEVICE_TOKEN); json.put("content_available",
 * true); json.put("priority", "high");
 * 
 * 
 * 
 * json.put("notification", jsonObjectData);
 * 
 * 
 * // System.out.println("json :" +jsonObjectData.toString()); //
 * System.out.println("jsonfinal :" +json);
 * 
 * // Create connection to send FCM Message request. URL url = new
 * URL("https://fcm.googleapis.com/fcm/send"); HttpURLConnection conn =
 * (HttpURLConnection) url.openConnection();
 * //conn.setRequestProperty("Authorization",
 * "key=AAAAgHRHE7M:APA91bFC5F9IZ5_8KZhNROpIeKwVMrdjA_SM7ugvYjgeFwn81G-DRG9syVxHWbVg_198OVhEcBmHpZv_PLIuc4Xw3etQm8_0L-MBoQiVGRpIP0s0R1f5zr2ESZI0wbhPLFjo487zN-Po"
 * );
 * 
 * conn.setRequestProperty("Authorization", "key="+SERVER_KEY); ///
 * conn.setRequestProperty("Content-Type", "application/json");
 * conn.setRequestMethod("POST"); conn.setDoOutput(true);
 * 
 * 
 * ObjectMapper objectMapper = new ObjectMapper();
 * 
 * // String json1 = objectMapper.writeValueAsString(json); //
 * System.out.println("Android Notification R "+objectMapper.writeValueAsString(
 * json)) ;
 * System.out.println("Android Notification R "+objectMapper.writeValueAsString(
 * json).toString()) ; // Send FCM message content.
 * 
 * // OutputStream outputStream = conn.getOutputStream(); //
 * outputStream.write(json.toString());
 * 
 * OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
 * wr.write(objectMapper.writeValueAsString(json).toString());
 * 
 * 
 * 
 * System.out.println("sendNotification res code--"+conn.getResponseCode());
 * System.out.println("sendNotification res msg--"+conn.getResponseMessage());
 * // System.out.println("sendNotification error msg--"+conn.getErrorStream().
 * toString());
 * 
 * // if( conn.getResponseCode() == 200 ){ //SUCCESS message BufferedReader
 * reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
 * System.out.println("Android Notification Response : " + reader.readLine());
 * // }
 */