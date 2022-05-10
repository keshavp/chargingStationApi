package com.scube.chargingstation.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import com.scube.chargingstation.dto.AuthUserDto;
import com.scube.chargingstation.dto.ChargingHistoryDto;
import com.scube.chargingstation.dto.ChargingHistoryRespDto;
import com.scube.chargingstation.dto.ChargingPointDto;
import com.scube.chargingstation.dto.RazorOrderIdDto;
import com.scube.chargingstation.dto.incoming.UserWalletRequestDto;
import com.scube.chargingstation.dto.mapper.ChargingHistoryMapper;
import com.scube.chargingstation.dto.mapper.ChargingPointMapper;
import com.scube.chargingstation.dto.mapper.RazorOrderIdMapper;
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
	ChargingPointService chargingPointService;

	@Autowired
	ConnectorService connectorService;
	
	
	RazorOrderIdMapper razorOrderIdMapper;

	private static final Logger logger = LoggerFactory.getLogger(UserPaymentServiceImpl.class);
	
	private static final DecimalFormat df = new DecimalFormat("0.00");

	@Value("${razorPayKey}")
	private String razorPayKey;

	@Value("${razorPaySecret}")
	private String razorPaySecret;

	@Override
	public boolean processWalletMoney(UserWalletRequestDto userWalletRequestDto) {
		// TODO Auto-generated method stub
		// Map<String, String> map = new HashMap<String, String>();

		UserWalletDtlEntity userWalletDtlEntity = new UserWalletDtlEntity();
		ChargingRequestEntity crEntity = null;
		
		df.setRoundingMode(RoundingMode.UP);


		UserInfoEntity userInfoEntity = userInfoRepository.findByMobilenumber(userWalletRequestDto.getMobileUser_Id());

		if (userInfoEntity == null) {
			throw BRSException.throwException("Error: User does not exist");
		}
		if ((!userWalletRequestDto.getTransactionType().equals("Debit"))
				&& (!userWalletRequestDto.getTransactionType().equals("Credit"))) {
			throw BRSException.throwException("Error: TransactionType is invalid");
		}

		if (userWalletRequestDto.getRequestAmount() == null)
			throw BRSException.throwException("Error: Amount can not be blank");

		if ((userWalletRequestDto.getRequestAmount().isEmpty())) {
			throw BRSException.throwException("Error: Amount can not be blank");
		}

		if ((userWalletRequestDto.getChargeRequestId() != null)
				&& (!userWalletRequestDto.getChargeRequestId().isEmpty())) {
			Optional<ChargingRequestEntity> chargingRequestEntity = chargingRequestRepository
					.findById(userWalletRequestDto.getChargeRequestId());
			crEntity = chargingRequestEntity.get();
		}
		Double amount = Double.parseDouble(userWalletRequestDto.getRequestAmount());

		userWalletDtlEntity.setTransactionType(userWalletRequestDto.getTransactionType());
		userWalletDtlEntity.setUserInfoEntity(userInfoEntity);
		userWalletDtlEntity.setChargingRequestEntity(crEntity);
		//userWalletDtlEntity.setAmount(amount);
		userWalletDtlEntity.setAmount(Double.parseDouble(df.format(amount)));
		// userWalletDtlEntity.setTransaction_id(userWalletRequestDto.getTransactionId());

		// save/update user wallet
		Double balance = 0.0;
		UserWalletEntity userWaltEntity = new UserWalletEntity();
		UserWalletEntity userchkWaltEntity = userWalletRepository.findByUserInfoEntity(userInfoEntity);

		if (userchkWaltEntity != null)
			userWaltEntity = userchkWaltEntity;

		UserWalletEntity userbalWaltEntity = userWalletRepository.findBalanceByUserId(userInfoEntity.getId());
		/*
		 * String currentBal = "0";
		 * 
		 * if (userbalWaltEntity != null) currentBal =
		 * userbalWaltEntity.getCurrentBalance();
		 */
		//Double curBal = Double.parseDouble(currentBal);
		
		Double curBal=userbalWaltEntity.getCurrentBalance();

		if (userWalletRequestDto.getTransactionType().equals("Credit")) {
			balance = curBal + amount;
		} else if (userWalletRequestDto.getTransactionType().equals("Debit")) {
			if (curBal < amount) {
				throw BRSException.throwException("Error: Insufficient balance");
			}
			balance = curBal - amount;
		}
		userWaltEntity.setUserInfoEntity(userInfoEntity);
		//userWaltEntity.setCurrentBalance(balance.toString());
		
	
	    System.out.println("after rounding off : " + Double.parseDouble(df.format(balance)));      //1205.64
	    
		userWaltEntity.setCurrentBalance(Double.parseDouble(df.format(balance)));
		userWalletRepository.save(userWaltEntity);

		userWalletDtlRepository.save(userWalletDtlEntity);

		return true;
	}

	@Override
	public Map<String, String> getMyWalletBalance(UserWalletRequestDto userWalletRequestDto) {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap<String, String>();
		Double balance = 0.0;

		UserInfoEntity userInfoEntity = userInfoRepository.findByMobilenumber(userWalletRequestDto.getMobileUser_Id());

		if (userInfoEntity == null) {
			throw BRSException.throwException("Error: User does not exist");
		}

		UserWalletEntity userWalletEntity = userWalletRepository.findByUserInfoEntity(userInfoEntity);
		if (userWalletEntity != null)
			balance = userWalletEntity.getCurrentBalance();

		map.put("balance", balance.toString());

		return map;
	}

	@Override
	public RazorOrderIdDto addWalletMoneyRequest(UserWalletRequestDto userWalletRequestDto) 
	{ 
		// TODO Auto-generated method stub
		df.setRoundingMode(RoundingMode.UP);
		razorOrderIdMapper=new RazorOrderIdMapper();
		RazorOrderIdDto response=new RazorOrderIdDto();
		
		UserWalletDtlEntity userWalletDtlEntity = new UserWalletDtlEntity();
		ChargingRequestEntity crEntity = null;

		UserInfoEntity userInfoEntity = userInfoRepository.findByMobilenumber(userWalletRequestDto.getMobileUser_Id());

		if (userInfoEntity == null) {
			throw BRSException.throwException("Error: User does not exist");
		}

		/*
		 * if ((!userWalletRequestDto.getTransactionType().equals("Debit")) &&
		 * (!userWalletRequestDto.getTransactionType().equals("Credit"))) { throw
		 * BRSException.throwException("Error: TransactionType is invalid"); }
		 */

		if (userWalletRequestDto.getRequestAmount() == null)
			throw BRSException.throwException("Error: Amount can not be blank");

		if ((userWalletRequestDto.getRequestAmount().isEmpty())) {
			throw BRSException.throwException("Error: Amount can not be blank");
		}

		
		String OrderId=createRazorOrderID(userWalletRequestDto);
		
		/*
		 * if ((userWalletRequestDto.getChargeRequestId() != null) &&
		 * (!userWalletRequestDto.getChargeRequestId().isEmpty())) {
		 * Optional<ChargingRequestEntity> chargingRequestEntity =
		 * chargingRequestRepository
		 * .findById(userWalletRequestDto.getChargeRequestId()); crEntity =
		 * chargingRequestEntity.get(); }
		 */

		//userWalletDtlEntity.setTransactionType(userWalletRequestDto.getTransactionType());
		userWalletDtlEntity.setTransactionType("Credit");
		userWalletDtlEntity.setUserInfoEntity(userInfoEntity);
		//userWalletDtlEntity.setChargingRequestEntity(crEntity);
		userWalletDtlEntity.setAmount(Double.parseDouble(df.format(Double.parseDouble(userWalletRequestDto.getRequestAmount()))));
		userWalletDtlEntity.setOrderId(OrderId);
		//userWalletDtlEntity.setTransaction_id(userWalletRequestDto.getTransactionId());
		userWalletDtlRepository.save(userWalletDtlEntity);

		response=razorOrderIdMapper.toRazorOrderIdDto(OrderId);
		
		return response;
	}

	@Override
	public String createRazorOrderID(UserWalletRequestDto userWalletRequestDto) {
		// TODO Auto-generated method stub

		String OrderId = "";
		Double amount = Double.parseDouble(userWalletRequestDto.getRequestAmount());
		
	//	Map<String, String> orderRequest = new HashMap<String, String>();
		JSONObject orderRequest = new JSONObject();
	//	orderRequest.put("amount", userWalletRequestDto.getRequestAmount());
		orderRequest.put("amount", amount*100);
		orderRequest.put("currency", "INR");
	//	orderRequest.put("receipt", "order_rcptid_11");

		ObjectMapper objectMapper = new ObjectMapper();
		// String json1 = objectMapper.writeValueAsString(orderRequest);
		// objectMapper.writeV
		RazorpayClient razorpay = null;
		try {
			razorpay = new RazorpayClient(razorPayKey, razorPaySecret);
			Order order = razorpay.Orders.create(orderRequest);
			logger.info("orderId for--" + order.get("id"));
			OrderId = order.get("id");
			if ((OrderId == null)|| (OrderId.isEmpty()))
			{
				throw BRSException.throwException("Error: In Razor Order Id Creation");
			}
			

		} catch (RazorpayException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
			throw BRSException.throwException("Error: In Razor Order Id Creation");

		}

		return OrderId;
	}
	
	
	@Override
	public boolean verifyRazorSignature(UserWalletRequestDto userWalletRequestDto) {
		// TODO Auto-generated method stub

		boolean flag = false;

		ObjectMapper objectMapper = new ObjectMapper();
		RazorpayClient razorpay = null;
		try {
			razorpay = new RazorpayClient(razorPayKey, razorPaySecret);
			JSONObject options = new JSONObject();
			
			options.put("razorpay_payment_id", userWalletRequestDto.getTransactionId());
			options.put("razorpay_order_id", userWalletRequestDto.getOrderId());
			//options.put("key_secret", razorPaySecret);
			options.put("razorpay_signature", userWalletRequestDto.getRazorSignature());
			
			flag=Utils.verifyPaymentSignature(options, razorPaySecret);
			
			logger.info("verifyRazorSignature flag--" + flag);
			
			if(!flag)
			{
				throw BRSException.throwException("Error: Invalid Razor Signature");
			}
			

		} catch (RazorpayException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
			throw BRSException.throwException("Error: In Razor Signature verification");

		}

		return flag;
	}

	@Override
	public boolean addWalletMoneyTransaction(@Valid UserWalletRequestDto userWalletRequestDto) {
		// TODO Auto-generated method stub
		UserInfoEntity userInfoEntity = userInfoRepository.findByMobilenumber(userWalletRequestDto.getMobileUser_Id());
		df.setRoundingMode(RoundingMode.UP);

		
		
		if (userInfoEntity == null)
		{
			throw BRSException.throwException("Error: User does not exist");
		}
		if (userWalletRequestDto.getRequestAmount() == null)
			throw BRSException.throwException("Error: Amount can not be blank");

		if ((userWalletRequestDto.getRequestAmount().isEmpty())) {
			throw BRSException.throwException("Error: Amount can not be blank");
		}
		
		if ((userWalletRequestDto.getTransactionId() == null)|| (userWalletRequestDto.getTransactionId().isEmpty()))
		{
			throw BRSException.throwException("Error: TransactionId can not be blank");
		}
		
		if ((userWalletRequestDto.getOrderId() == null)|| (userWalletRequestDto.getOrderId().isEmpty()))
		{
			throw BRSException.throwException("Error: OrderId can not be blank");
		}
		
		if ((userWalletRequestDto.getRazorSignature() == null)|| (userWalletRequestDto.getRazorSignature().isEmpty()))
		{
			throw BRSException.throwException("Error: RazorSignature can not be blank");
		}
		
		
		  if(!verifyRazorSignature(userWalletRequestDto)) { throw
		  BRSException.throwException("Error: Invalid Razor Signature"); }
		 
		UserWalletDtlEntity userWalletDtlEntity=userWalletDtlRepository.findByUserInfoEntityAndOrderId(userInfoEntity, userWalletRequestDto.getOrderId());
		
		if(userWalletDtlEntity==null)
		{
			throw BRSException.throwException("Error: Invalid OrderId");
		}
		userWalletDtlEntity.setRazorSignature(userWalletRequestDto.getRazorSignature());
		userWalletDtlEntity.setTransactionId(userWalletRequestDto.getTransactionId());
		userWalletDtlEntity.setAmount(Double.parseDouble(df.format(Double.parseDouble(userWalletRequestDto.getRequestAmount()))));
		

		
		userWalletDtlRepository.save(userWalletDtlEntity);
		
		Double balance = 0.0;
		UserWalletEntity userWaltEntity = new UserWalletEntity();

		UserWalletEntity userchkWaltEntity = userWalletRepository.findByUserInfoEntity(userInfoEntity);

		if (userchkWaltEntity != null)
			userWaltEntity = userchkWaltEntity;

		
		Double amount = Double.parseDouble(userWalletRequestDto.getRequestAmount());
		UserWalletEntity userbalWaltEntity = userWalletRepository.findBalanceByUserId(userInfoEntity.getId());
		Double curBal=0.0;
		
		if(userbalWaltEntity!=null)
		{
			curBal = userbalWaltEntity.getCurrentBalance();
		}
		
		balance = curBal + amount;

		userWaltEntity.setUserInfoEntity(userInfoEntity);
		userWaltEntity.setCurrentBalance(balance);
		userWalletRepository.save(userWaltEntity);
		
		return true;
	}

	@Override
	public List<ChargingHistoryRespDto> getChargingTrHistory(UserWalletRequestDto userWalletRequestDto) {
		// TODO Auto-generated method stub
		
		logger.info("userInfo"+userWalletRequestDto.getMobileUser_Id());
		
		UserInfoEntity userInfoEntity = userInfoRepository.findByMobilenumber(userWalletRequestDto.getMobileUser_Id());
		if (userInfoEntity == null)
		{
			throw BRSException.throwException("Error: User does not exist");
			
		}
		List<Map<String, String>> listDtl=userWalletDtlRepository.getUserTrHistory(userInfoEntity.getId());
		
		List<ChargingHistoryRespDto> chargingHistoryDtoLst = ChargingHistoryMapper.toChargingHistoryDto(listDtl);
		
		return chargingHistoryDtoLst;
	}

	//

	/*
	 * @Override public boolean addUserWalletDetail(UserWalletRequestDto
	 * userWalletRequestDto) { // TODO Auto-generated method stub
	 * 
	 * 
	 * UserWalletDtlEntity userWalletDtlEntity=new UserWalletDtlEntity();
	 * ChargingRequestEntity crEntity=null;
	 * 
	 * UserInfoEntity userInfoEntity =
	 * userInfoRepository.findByMobilenumber(userWalletRequestDto.getMobileUser_Id()
	 * );
	 * 
	 * if(userInfoEntity==null) { throw
	 * BRSException.throwException("Error: User does not exist"); }
	 * 
	 * if((!userWalletRequestDto.getTransactionType().equals("Debit"))&&(!
	 * userWalletRequestDto.getTransactionType().equals("Credit"))) { throw
	 * BRSException.throwException("Error: TransactionType is invalid"); }
	 * 
	 * if(userWalletRequestDto.getRequestAmount()==null) throw
	 * BRSException.throwException("Error: Amount can not be blank");
	 * 
	 * if((userWalletRequestDto.getRequestAmount().isEmpty())) { throw
	 * BRSException.throwException("Error: Amount can not be blank"); }
	 * 
	 * if((userWalletRequestDto.getChargeRequestId()!=null)&&(!userWalletRequestDto.
	 * getChargeRequestId().isEmpty())) { Optional<ChargingRequestEntity>
	 * chargingRequestEntity=chargingRequestRepository.findById(userWalletRequestDto
	 * .getChargeRequestId()); crEntity = chargingRequestEntity.get(); }
	 * 
	 * 
	 * userWalletDtlEntity.setTransactionType(userWalletRequestDto.
	 * getTransactionType()); userWalletDtlEntity.setUserInfoEntity(userInfoEntity);
	 * userWalletDtlEntity.setChargingRequestEntity(crEntity);
	 * userWalletDtlEntity.setAmount(userWalletRequestDto.getRequestAmount());
	 * userWalletDtlEntity.setTransaction_id(userWalletRequestDto.getTransactionId()
	 * );
	 * 
	 * 
	 * //save/update user wallet
	 * 
	 * Double balance=0.0; UserWalletEntity userWaltEntity=new UserWalletEntity();
	 * 
	 * UserWalletEntity
	 * userchkWaltEntity=userWalletRepository.findByUserInfoEntity(userInfoEntity);
	 * 
	 * 
	 * if(userchkWaltEntity!=null) userWaltEntity=userchkWaltEntity;
	 * 
	 * Double amount=Double.parseDouble(userWalletRequestDto.getRequestAmount());
	 * 
	 * UserWalletEntity
	 * userbalWaltEntity=userWalletRepository.findBalanceByUserId(userInfoEntity.
	 * getId()); String currentBal="0";
	 * 
	 * if(userbalWaltEntity!=null) currentBal=userbalWaltEntity.getCurrentBalance();
	 * 
	 * Double curBal=Double.parseDouble(currentBal);
	 * 
	 * if(userWalletRequestDto.getTransactionType().equals("Credit")) {
	 * balance=curBal+amount; } else
	 * if(userWalletRequestDto.getTransactionType().equals("Debit")) {
	 * if(curBal<amount) { throw
	 * BRSException.throwException("Error: Insufficient balance"); }
	 * balance=curBal-amount; }
	 * 
	 * 
	 * userWaltEntity.setUserInfoEntity(userInfoEntity);
	 * userWaltEntity.setCurrentBalance(balance.toString());
	 * userWalletRepository.save(userWaltEntity);
	 * 
	 * 
	 * userWalletDtlRepository.save(userWalletDtlEntity);
	 * 
	 * 
	 * 
	 * return true; }
	 */

	///

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
