package com.scube.chargingstation.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.scube.chargingstation.dto.incoming.UserWalletRequestDto;
import com.scube.chargingstation.entity.ChargingRequestEntity;
import com.scube.chargingstation.entity.UserInfoEntity;
import com.scube.chargingstation.entity.UserWalletDtlEntity;
import com.scube.chargingstation.entity.UserWalletEntity;
import com.scube.chargingstation.exception.BRSException;
import com.scube.chargingstation.repository.ChargerTypeRepository;
import com.scube.chargingstation.repository.ChargingPointRepository;
import com.scube.chargingstation.repository.ChargingRequestRepository;
import com.scube.chargingstation.repository.UserInfoRepository;
import com.scube.chargingstation.repository.UserWalletDtlRepository;
import com.scube.chargingstation.repository.UserWalletRepository;

@Service
public class UserPaymentServiceImpl implements UserPaymentService {

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
	
	
	
	@Override
	public boolean processWalletMoney(UserWalletRequestDto userWalletRequestDto) {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap<String, String>();

		
		UserWalletDtlEntity userWalletDtlEntity=new UserWalletDtlEntity();
		ChargingRequestEntity crEntity=null;
		
		UserInfoEntity userInfoEntity = userInfoRepository.findByMobilenumber(userWalletRequestDto.getMobileUser_Id());
		
		if(userInfoEntity==null) { 
			throw BRSException.throwException("Error: User does not exist"); 
		}
		
		if((!userWalletRequestDto.getTransactionType().equals("Debit"))&&(!userWalletRequestDto.getTransactionType().equals("Credit")))
		{
			throw BRSException.throwException("Error: TransactionType is invalid"); 
		}
		
		if(userWalletRequestDto.getRequestAmount()==null)
			throw BRSException.throwException("Error: Amount can not be blank"); 
			
		if((userWalletRequestDto.getRequestAmount().isEmpty()))
		{
			throw BRSException.throwException("Error: Amount can not be blank"); 
		}
		
		if((userWalletRequestDto.getChargeRequestId()!=null)&&(!userWalletRequestDto.getChargeRequestId().isEmpty()))
		{
		Optional<ChargingRequestEntity> chargingRequestEntity=chargingRequestRepository.findById(userWalletRequestDto.getChargeRequestId());
		crEntity = chargingRequestEntity.get();
		}
		
		
		userWalletDtlEntity.setTransactionType(userWalletRequestDto.getTransactionType());
		userWalletDtlEntity.setUserInfoEntity(userInfoEntity);
		userWalletDtlEntity.setChargingRequestEntity(crEntity);
		userWalletDtlEntity.setAmount(userWalletRequestDto.getRequestAmount());
		userWalletDtlEntity.setTransaction_id(userWalletRequestDto.getTransactionId());
		
		
	  //save/update user wallet
			Double balance=0.0;
			UserWalletEntity userWaltEntity=new UserWalletEntity();
		
			UserWalletEntity userchkWaltEntity=userWalletRepository.findByUserInfoEntity(userInfoEntity);
		 
		 
			if(userchkWaltEntity!=null)
				userWaltEntity=userchkWaltEntity;
			
		 Double amount=Double.parseDouble(userWalletRequestDto.getRequestAmount());
		 
		 UserWalletEntity userbalWaltEntity=userWalletRepository.findBalanceByUserId(userInfoEntity.getId());
		 String currentBal="0";
		 
		 if(userbalWaltEntity!=null)
			 currentBal=userbalWaltEntity.getCurrentBalance();
		 
		 Double curBal=Double.parseDouble(currentBal);
		 
		  if(userWalletRequestDto.getTransactionType().equals("Credit"))
		  {
			  balance=curBal+amount;
		  } 
		  else if(userWalletRequestDto.getTransactionType().equals("Debit")) 
		  {
			if(curBal<amount)
				 { 
					throw BRSException.throwException("Error: Insufficient balance"); 
				}
		  balance=curBal-amount;
		  } 
		  
		  
		  userWaltEntity.setUserInfoEntity(userInfoEntity);
		  userWaltEntity.setCurrentBalance(balance.toString());
		  userWalletRepository.save(userWaltEntity);
		//
		
		
		userWalletDtlRepository.save(userWalletDtlEntity);
		
		
		
		return true;
	}

	@Override
	public Map<String, String> getMyWalletBalance(UserWalletRequestDto userWalletRequestDto) {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap<String, String>();
		String balance="0";
		
		UserInfoEntity userInfoEntity = userInfoRepository.findByMobilenumber(userWalletRequestDto.getMobileUser_Id());
		
		if(userInfoEntity==null) { 
			throw BRSException.throwException("Error: User does not exist"); 
		}

		UserWalletEntity userWalletEntity = userWalletRepository.findByUserInfoEntity(userInfoEntity);
			if(userWalletEntity!=null)
			balance=userWalletEntity.getCurrentBalance();
		
		map.put("balance", balance);
		
		return map;
	}

	
	/*
	 * boolean sendNotification(int userId) { String SERVER_KEY =
	 * "AAAAgHRHE7M:APA91bFC5F9IZ5_8KZhNROpIeKwVMrdjA_SM7ugvYjgeFwn81G-DRG9syVxHWbVg_198OVhEcBmHpZv_PLIuc4Xw3etQm8_0L-MBoQiVGRpIP0s0R1f5zr2ESZI0wbhPLFjo487zN-Po";
	 * boolean flag=false;
	 * 
	 * String DEVICE_TOKEN = "";
	 * 
	 * String title = "CS Notification"; String message = "charging status";
	 * 
	 * try { DEVICE_TOKEN=
	 * "f_9giX9FSxygKuJtAAFcrE:APA91bFIIgzI20aoi6wI_xlsZ7LsWwob2UhwcqGTqeHywBHGqopYhhApMUUBgML9LSvIu6FIiiXt7RqF2E_HDMTbUwfjGGTsXyITqR4Qny0k73ujlVcv3_cZHCl2peplY9ptC8W3wVJJ";
	 * 
	 * System.out.println("DEVICE_TOKEN--"+DEVICE_TOKEN);
	 * 
	 * Map<String, Object> jsonObjectData = new HashMap<String, Object>();
	 * jsonObjectData.put("title", "title"); jsonObjectData.put("body",
	 * "testing notification");
	 * 
	 * 
	 * Map<String, Object> json = new HashMap<String, Object>(); json.put("to",
	 * DEVICE_TOKEN); json.put("notification", jsonObjectData);
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
	 * conn.setRequestProperty("Authorization", "key="+SERVER_KEY); //
	 * conn.setRequestProperty("Content-Type", "application/json");
	 * conn.setRequestMethod("POST"); conn.setDoOutput(true);
	 * 
	 * 
	 * ObjectMapper objectMapper = new ObjectMapper();
	 * 
	 * // String json1 = objectMapper.writeValueAsString(json); //
	 * System.out.println("Android Notification R "+objectMapper.writeValueAsString(
	 * json)) ; //
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
	 * if( conn.getResponseCode() == 200 ){ //SUCCESS message BufferedReader reader
	 * = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	 * System.out.println("Android Notification Response : " + reader.readLine()); }
	 * 
	 * 
	 * 
	 * 
	 * flag=true;
	 * 
	 * } catch(Exception e) { System.out.println("exception occured"+e.toString());
	 * e.printStackTrace(); }
	 * 
	 * return flag; }
	 */
	

	
}
