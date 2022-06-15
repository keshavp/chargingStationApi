package com.scube.chargingstation.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scube.chargingstation.dto.AmenityDto;
import com.scube.chargingstation.dto.ChargingHistoryDto;
import com.scube.chargingstation.dto.ChargingPointDto;
import com.scube.chargingstation.dto.ChargingRequestRespDto;
import com.scube.chargingstation.dto.ChargingStatusRespDto;
import com.scube.chargingstation.dto.MostActiveChargingStationsDto;
import com.scube.chargingstation.dto.UserDashboardDto;
import com.scube.chargingstation.dto.ChargingPointConnectorDto;
import com.scube.chargingstation.dto.ChargingPointConnectorRateDto;
import com.scube.chargingstation.dto.incoming.ChargingRequestDto;
import com.scube.chargingstation.dto.incoming.ChargingStationDto;
import com.scube.chargingstation.dto.incoming.ChargingStationWiseReportIncomingDto;
import com.scube.chargingstation.dto.incoming.NotificationReqDto;
import com.scube.chargingstation.dto.mapper.AmenityMapper;
import com.scube.chargingstation.dto.mapper.ChargingPointMapper;
import com.scube.chargingstation.dto.mapper.ChargingRequestMapper;
import com.scube.chargingstation.dto.mapper.ChargingStatusMapper;
import com.scube.chargingstation.dto.mapper.ConnectorMapper;
import com.scube.chargingstation.entity.AmenitiesEntity;
import com.scube.chargingstation.entity.CarModelEntity;
import com.scube.chargingstation.entity.ChargerTypeEntity;
import com.scube.chargingstation.entity.ChargingPointEntity;
import com.scube.chargingstation.entity.ChargingRequestEntity;
import com.scube.chargingstation.entity.ConnectorEntity;
import com.scube.chargingstation.entity.TransactionsEntity;
import com.scube.chargingstation.entity.UserInfoEntity;
import com.scube.chargingstation.entity.UserWalletEntity;
import com.scube.chargingstation.exception.BRSException;
import com.scube.chargingstation.repository.CarModelRepository;
import com.scube.chargingstation.repository.ChargerTypeRepository;
import com.scube.chargingstation.repository.ChargingPointRepository;
import com.scube.chargingstation.repository.ChargingRequestRepository;
import com.scube.chargingstation.repository.TransactionsRepository;
import com.scube.chargingstation.repository.UserInfoRepository;
import com.scube.chargingstation.repository.UserWalletDtlRepository;
import com.scube.chargingstation.repository.UserWalletRepository;
import com.scube.chargingstation.util.StaticPathContUtils;
import com.scube.chargingstation.util.StringNullEmpty;

@Service
public class ChargingRequestServiceImpl implements ChargingRequestService {

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
	
	@Autowired
	NotificationService notificationService;
	
	@Autowired
	CarModelRepository carModelRepository;
	
	@Autowired
	ChargingPointConnectorRateService chargingPointConnectorRateService;
	
	@Autowired
	TransactionsRepository transactionsRepository;
	
	
	
	 @Value("${chargingstation.chargertype}")
	private   String imgLocation;
	 
	private static final Logger logger = LoggerFactory.getLogger(ChargingRequestServiceImpl.class);

	 
	
	@Override
	public boolean addChargingRequest(ChargingRequestDto chargingRequestDto) {
			
			ChargingRequestEntity chargingRequestEntity  = new  ChargingRequestEntity();
			String connectorId= Integer.toString(chargingRequestDto.getConnectorId());
			
			UserInfoEntity userInfoEntity = userInfoRepository.findByMobilenumber(chargingRequestDto.getMobileUser_Id());
			if(userInfoEntity==null)
			{
				throw BRSException.throwException("Error: User does not exist"); 
			}
			
			ChargingPointEntity	chargingPointEntity = chargingPointService.getChargingPointEntityByChargingPointId(chargingRequestDto.getChargePointId());
			if(chargingPointEntity==null)
			{
				throw BRSException.throwException("Error: Charging Point does not exist"); 
			}
			
			
			ConnectorEntity	connectorEntity = connectorService.getConnectorEntityById(connectorId) ;
			if(connectorEntity==null)
			{
				throw BRSException.throwException("Error: Connector does not exist"); 
			}
			
			UserWalletEntity userWalletEntity=userWalletRepository.findByUserInfoEntity(userInfoEntity);
			if(userWalletEntity==null)
			{
				  throw BRSException.throwException("Error: Insufficient Wallet Balance");
			}
				
			Double balance=0.0;
		//	String curBal=userWalletEntity.getCurrentBalance(); //existing balance
		//	Double dCurBal=Double.parseDouble(curBal); //double existing balance
			
			Double dCurBal=userWalletEntity.getCurrentBalance();

			Double reqAmt=Double.parseDouble(chargingRequestDto.getRequestAmount()); //requested charging amount
			
			if((reqAmt==0)||(reqAmt==0.0))//full charge sceanario
			{
				if(dCurBal<1000)
				{
					  throw BRSException.throwException("Error: FullCharge needs 1000 INR as Wallet Balance");
				}
			}
			
			if(dCurBal<reqAmt)
			{
				  throw BRSException.throwException("Error: Insufficient Wallet Balance");
			}
			
			//List<ChargingRequestEntity> list=chargingRequestRepository.findMyPendingChargingRequests(chargingPointEntity, connectorEntity, userInfoEntity);
			List<ChargingRequestEntity> list=chargingRequestRepository.findMyPendingChargingRequests(chargingPointEntity, connectorEntity);
		  	if(list.size()>0)
			{
			  throw BRSException.throwException("Error: You are in Queue please try after few minutes");
			}
			
		    List<ChargingRequestEntity> listP=chargingRequestRepository.findMyOnGoingChargingProcesses(chargingPointEntity, connectorEntity, userInfoEntity);
		  	if(listP.size()>0)
			{
			  throw BRSException.throwException("Error: Can't book, your charging request is already in process");
			}
		  	// to call remote start API need transaction Id and allowedcharge value
		  	
		 	Double chrg=0.0;
		  	chrg= getAllowedCharge(chargingRequestDto);
		  	
		  	String transactionId="";
		  	
		  	transactionId=callRemoteStartAPI(chargingRequestDto,chrg);
		  	
		  	logger.info("remote start response"+transactionId);
		  	
		  	if(transactionId.equals("Offline"))  //remote start first returns connector offline 
		  	{
		  		//call hardreset
		  		logger.info("before hardreset");
		  		String strRspnse=callHardResetConnectorAPI(chargingRequestDto);
			  	logger.info("hardreset response"+strRspnse);

		  		if(strRspnse.equals("OK"))
		  		{
		  			logger.info("second attempt of callRemoteStartAPI");
		  			transactionId=callRemoteStartAPI(chargingRequestDto,chrg);
		  			logger.info("remote start 2nd attempt response"+transactionId);
		  			
		  			if(transactionId.equals("Offline"))  //remote start first returns connector offline 
				  	{
		  				 throw  BRSException.throwException("Error: Can't book, Remote start second attempt error Offline "); 
				  	}
		  			else if(transactionId.equals("Running"))  //remote start first returns connector offline 
					 {
			  				 throw  BRSException.throwException("Error: Can't book, Remote start second attempt error Running "); 
					  }
		  		}
		  	}
		  	
		  	if(transactionId.equals("Running"))  //remote start first returns connector running 
		  	{
		  		//call hardreset
		  		logger.info("before reset");
		  		
		  		ChargingRequestEntity centity= new ChargingRequestEntity();
		  		centity.setChargingPointEntity(chargingPointEntity);
		  		centity.setConnectorEntity(connectorEntity);
		  		
		  		String strRspnse=callResetConnectorAPI(centity);
			  	logger.info("reset connector response"+strRspnse);

		  		if(strRspnse.equals("OK"))
		  		{
		  			logger.info("second attempt of callRemoteStartAPI after reset");
		  			transactionId=callRemoteStartAPI(chargingRequestDto,chrg);
		  			
		  			logger.info("remote start 2nd attempt response after reset"+transactionId);
		  			
		  			if(transactionId.equals("Offline"))  //remote start first returns connector offline 
				  	{
		  				 throw  BRSException.throwException("Error: Can't book, Remote start second attempt error Offline "); 
				  	}else
		  			if(transactionId.equals("Running"))  //remote start first returns connector offline 
				  	{
		  				 throw  BRSException.throwException("Error: Can't book, Remote start second attempt error Running "); 
				  	}
		  		}
		  	}	
		  	
		
		  TransactionsEntity transactionsEntity  =transactionsRepository.findByTransactionId(Integer.parseInt(transactionId));
		  
		  if(transactionsEntity!=null)
		  chargingRequestEntity.setTransactionsEntity(transactionsEntity);
		  else 
		  {
			  throw  BRSException.throwException("Error: Can't book, transactionId can not be blank "); 
			  }
		 
		  	
		  	chargingRequestEntity.setRequestKwh(chrg);
			chargingRequestEntity.setChargingPointEntity(chargingPointEntity);
			chargingRequestEntity.setConnectorEntity(connectorEntity);
			chargingRequestEntity.setUserInfoEntity(userInfoEntity);
			chargingRequestEntity.setStatus(chargingRequestDto.getStatus());
			chargingRequestEntity.setRequestAmount(Double.valueOf(chargingRequestDto.getRequestAmount()));
			chargingRequestEntity.setChargingStatus("Pending");
			
			chargingRequestEntity.setCustName(chargingRequestDto.getName());
			chargingRequestEntity.setMobileNo(chargingRequestDto.getMobileNo());
			chargingRequestEntity.setVehicleNO(chargingRequestDto.getVechicleNo());
			
			
			chargingRequestEntity.setIsdeleted("N");
			
			chargingRequestRepository.save(chargingRequestEntity);
			
			return true;
			
		}

	@Override
	public List<ChargingRequestEntity> findChargingRequestEntityByChargingStatus(String chargingStatus) {
		// TODO Auto-generated method stub
		
		return chargingRequestRepository.findByChargingStatus("Starting");
	}

	@Override
	public ChargingRequestEntity findChargingRequestEntityByChargingPointEntityAndConnectorEntityAndStatus(
			ChargingPointEntity chargingPointEntity, ConnectorEntity connectorEntity, String string) {
		// TODO Auto-generated method stub
		
		return chargingRequestRepository.findByChargingPointEntityAndConnectorEntityAndStatus(chargingPointEntity, connectorEntity,"REQUESTED");
	}
	
	

	
	
	  @Override public List<ChargingPointDto>
	  getNearByChargingStationsOld(ChargingStationDto chargingStationDto) 
	  { 
	  
	  List<ChargingPointEntity> cpEntityLst=chargingPointService.getAllChargingPointEntity();
	  
	  List<ChargingPointDto> chargingPointDtoLst =
	  ChargingPointMapper.toChargingPointDto(cpEntityLst); 
	  return  chargingPointDtoLst;
	  
	 } 
	 
	  @Override public List<ChargingPointDto>  getNearByChargingStations(ChargingStationDto chargingStationDto) { 
		  // TODO   Auto-generated method stub
	  
	  String carModelId=chargingStationDto.getCarModelId();
	  Set<ChargerTypeEntity>  chargerTypes=null;
	  
	  if(carModelId!=null&&!carModelId.isEmpty()) 
	  { 
		  Optional<CarModelEntity> cmEntity=carModelRepository.findById(carModelId); 
		  CarModelEntity  entity=cmEntity.get();
	  
		  if(entity==null) 
		  { 
			  throw	  BRSException.throwException("Error: This vehicle model does not exist");
		  }
		  if(entity!=null)
		  { 
			  chargerTypes=entity.getChargertypes(); //selected car's charger types
		  }
	  
	  }
	  
	  List<ChargingPointEntity> cpEntityLst=chargingPointService.getAllChargingPointEntity();
	  List<ChargingPointDto> chargingPointDtoLst=new ArrayList<ChargingPointDto>();
	  
	  for(ChargingPointEntity chargingPointEntity : cpEntityLst) { 
		  ChargingPointDto   CPDto=new ChargingPointDto();
	  
	  Set<ChargingPointConnectorDto> connectors=new HashSet<ChargingPointConnectorDto>(); 
	  Set<AmenityDto> amenities =new HashSet<AmenityDto>();
	  
	  Set<ConnectorEntity> conEntitySet 	  =chargingPointEntity.getConnectorEntities();
	  Set<AmenitiesEntity> ameEntitySet= chargingPointEntity.getAmenities();
	  
	  for(ConnectorEntity conEntity : conEntitySet) 
	  { 
		  ChargingPointConnectorDto conDto=new  ChargingPointConnectorDto(); 
		  //conDto=ConnectorMapper.toConnectorDto(conEntity);
		  conEntity.getChargerTypeEntity().getId();	 
		  
		  if(chargerTypes==null) //input car is not selected
		  {
			  conDto.setCompatible("Y");
		  }
		  else if(chargerTypes.isEmpty()) //input car is not selected
		  {
			  conDto.setCompatible("Y");
		  }   
		  else
		  {
			  if(chargerTypes.contains(conEntity.getChargerTypeEntity()))   
			  {
					logger.info("***Compatible***");
					conDto.setCompatible("Y");
			  }  
			  else
			  {
					logger.info("***not Compatible***");
					conDto.setCompatible("N");
			  }
		  }
	//  conDto.setAvailable(conEntity.getConnectorStatusEntity().getLastStatus().equals("Available") ? "N":"Y");
	// as per lateest discussion if connector status shows status as Charging then user can not book request	  
	  conDto.setAvailable(conEntity.getConnectorStatusEntity().getLastStatus().equals("Charging") ? "N":"Y");		  
	  conDto.setConnectorId(conEntity.getConnectorId()); //
	  conDto.setChargingPoint(conEntity.getChargingPointEntity().getChargingPointId()); 
	  conDto.setChargerId(conEntity.getChargerTypeEntity().getId());
	  conDto.setChargerType(conEntity.getChargerTypeEntity().getName());
	  conDto.setImage(StaticPathContUtils.APP_URL_DIR+StaticPathContUtils.SET_CHARGER_TYPE_FILE_URL_DIR+conEntity.getChargerTypeEntity().getId());
	  connectors.add(conDto); 
	  }
	  
	  for(AmenitiesEntity amentyEntity : ameEntitySet)
	  { 
		  AmenityDto ameDto=new  AmenityDto();
		  ameDto=AmenityMapper.toAmenityDto(amentyEntity);
		  amenities.add(ameDto); 
	  }
	  
	  CPDto.setAddress(chargingPointEntity.getAddress());
	  
	//  Double dist=0.0;
	  
	//  CPDto.setDistance(StringNullEmpty.stringNullAndEmptyToBlank(chargingPointEntity.getDistance()));
	  CPDto.setDistance(0.0);
	  CPDto.setLatitude(chargingPointEntity.getLatitude());
	  CPDto.setLongitude(chargingPointEntity.getLongitude());
	  CPDto.setName(chargingPointEntity.getName());
	  CPDto.setRating(chargingPointEntity.getRating());
	  CPDto.setStatus(chargingPointEntity.getStatus());
	  CPDto.setChargingPointId(chargingPointEntity.getChargingPointId());
	  CPDto.setConnectors(connectors); CPDto.setAmenities(amenities);
	  chargingPointDtoLst.add(CPDto);
	  
	  }
	  
	//  callRemoteStartAPI(null,null);
	  
	  // List<ChargingPointDto> chargingPointDtoLst =  ChargingPointMapper.toChargingPointDto(cpEntityLst); 
	  return   chargingPointDtoLst;
	  
	  }
	 

	@Override
	public List<ChargingStatusRespDto> getChargingStatus(ChargingRequestDto chargingRequestDto) {
		// TODO Auto-generated method stub
		
		UserInfoEntity userInfoEntity = userInfoRepository.findByMobilenumber(chargingRequestDto.getMobileUser_Id());
		if(userInfoEntity==null)
		{
			throw BRSException.throwException("Error: User does not exist"); 
		}
		
		List<Map<String, String>> listDtl=chargingRequestRepository.getUserChargingStatus(userInfoEntity.getId());
		List<ChargingStatusRespDto> chargingStatusRespDtoLst = ChargingStatusMapper.toChargingStatusRespDto(listDtl);
		return chargingStatusRespDtoLst;
		
	}

	
	@Override
	public void timeoutPendingChargingRequests() {
		// TODO Auto-generated method stub
		List<ChargingRequestEntity> chargingRequestEntityList =  chargingRequestRepository.findByChargingStatusAndCreatedMinutes("Pending","5");
		for(ChargingRequestEntity chargingRequestEntity : chargingRequestEntityList) 
		{
			chargingRequestEntity.setChargingStatus("Rejected");
			chargingRequestEntity.setStatus("TimeOut");
			chargingRequestRepository.save(chargingRequestEntity);
			
			logger.info("***sending booking timeout Notification for***"+chargingRequestEntity.getUserInfoEntity().getMobilenumber());
			
			String title="Thank you for using EV-Dock.";
			String body="Your Booking for charging is time out";
			NotificationReqDto notificationReqDto =new NotificationReqDto();
			notificationReqDto.setMobileUser_Id(chargingRequestEntity.getUserInfoEntity().getMobilenumber());
			notificationReqDto.setTitle(title);
			notificationReqDto.setBody(body);
			
			try 
			{
				notificationService.sendNotification(notificationReqDto);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				logger.info("***exception in timeoutPendingChargingRequests***"+e.toString());
			}
			
		}
	}
	
	
	@Override
	public void sendGunInsertNotification() {
		// TODO Auto-generated method stub
		List<ChargingRequestEntity> chargingRequestEntityList =  chargingRequestRepository.findByChargingStatusAndCreatedMinutes("Pending","3");
		for(ChargingRequestEntity chargingRequestEntity : chargingRequestEntityList) 
		{
			
			UserInfoEntity userInfoEntity = userInfoRepository.findById(chargingRequestEntity.getUserInfoEntity().getId());
			if(userInfoEntity==null)
			{
				throw BRSException.throwException("Error: User does not exist"); 
			}
			logger.info("***sending sendGunInsertNotification for***"+chargingRequestEntity.getUserInfoEntity().getMobilenumber());
			
			String title="Thank you for using EV-Dock.";
			String body="Please insert the gun";
			NotificationReqDto notificationReqDto =new NotificationReqDto();
			notificationReqDto.setMobileUser_Id(chargingRequestEntity.getUserInfoEntity().getMobilenumber());
			notificationReqDto.setTitle(title);
			notificationReqDto.setBody(body);

			try 
			{
				callResetConnectorAPI(chargingRequestEntity);
			//	notificationService.sendNotification(notificationReqDto);
				//call ocpp server reset connector API to free the connector
				
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
				logger.info("***exception in sendGunInsertNotification***"+e.toString());
			}

			
		}
		
		timeoutPendingChargingRequests();
		
		
		
		
		
	}

	@Override
	public ChargingRequestRespDto getChargingRequestDetails(ChargingRequestDto chargingRequestDto) {
		// TODO Auto-generated method stub
		
		if(chargingRequestDto.getChargingRequestId()==null)
		{
			throw BRSException.throwException("Error: ChargingRequestId can not be blank"); 

		}
		
		Optional<ChargingRequestEntity> chargingRequestEntity=chargingRequestRepository.findById(chargingRequestDto.getChargingRequestId());
		ChargingRequestEntity entity=chargingRequestEntity.get();
		
		if(entity==null)
		{
			throw BRSException.throwException("Error: NO Charging request present"); 

		}
		
		//ChargingRequestRespDto chargingRequestRespDto=new ChargingRequestRespDto();
		
		ChargingRequestMapper.toChargingRequestRespDto(entity);
		
		return ChargingRequestMapper.toChargingRequestRespDto(entity);
	}
	

	public String callRemoteStartAPI(ChargingRequestDto chargingRequestDto,Double allowdChrg) 
	{
		URL getUrl = null;
		//Double allowdChrg=0.0;
	    String strResponse = null;
		try 
		{
		//	allowdChrg=getAllowedCharge(chargingRequestDto);
			getUrl = new URL(StaticPathContUtils.SERVER_API_URL+"RemoteStart/"+chargingRequestDto.getChargePointId()+"/"+chargingRequestDto.getConnectorId()+"/"+allowdChrg);
		//	getUrl = new URL("http://125.99.153.126:8080/API/RemoteStart/1347212300231/1/.05");
		//	getUrl = new URL("http://125.99.153.126:8080/API/RemoteStart/TACW2242321G0285/1/.05");
			
			HttpURLConnection conection;
			conection = (HttpURLConnection) getUrl.openConnection();
			conection.setRequestMethod("GET");
	     
	        logger.info("ResponseMessage="+conection.getResponseMessage());
	        logger.info("responseCode="+conection.getResponseCode());
	       
	        BufferedReader br = null;
	        br = new BufferedReader(new InputStreamReader((conection.getInputStream())));

	        if (conection.getResponseCode()==200 && conection.getResponseMessage().equals("OK") ) {
	            br = new BufferedReader(new InputStreamReader(conection.getInputStream()));
	        } else {
	        	
				throw BRSException.throwException("Error: in RemoteStart API call "+conection.getResponseCode()); 
	        }
	        
	        logger.info("br="+br.toString());
	        StringBuilder response = new StringBuilder();
	        
	        while ((strResponse = br.readLine()) != null) 
	            response.append(strResponse);

	        br.close();
	        logger.info("API response string="+response);
	        
	        final ObjectMapper mapper = new ObjectMapper();
	        JsonNode actualObj = mapper.readTree(response.toString());
	        
	        logger.info("response111="+actualObj.get("Status").textValue());
	        logger.info("response222="+actualObj.get("Payload").textValue());
	        
	        
	        if((actualObj.get("Status").textValue()).equals("OK"))
	        {
	        	strResponse=actualObj.get("Payload").textValue();
	        }
	        else if((actualObj.get("Status").textValue()).equals("Error"))
	        {
				
	        	 strResponse=actualObj.get("Payload").textValue();
	        	
	        	
	        	if(strResponse.contains("running"))
	        	{
	        		strResponse = "Running";
	        	}
	        	else if(strResponse.contains("offline"))
	        	{
	        		strResponse = "Offline";
	        	}
	        	else
	        	{
		        	throw BRSException.throwException("Error: in RemoteStart API call "+actualObj.get("Payload")); 

	        	}
	        	//if already running transaction
	        	
	        	//if offline
				
	        }
	        else
	        {
				throw BRSException.throwException("Error: in RemoteStart API call "); 
	        }
	        
		} 
	
	  catch (IOException e) { // TODO Auto-generated catch block e.printStackTrace();
		  		throw BRSException.throwException("Error: RemoteStart API");
	  
	  }
	 

        // Getting response code
		return strResponse;
		
	}
	
	public Double getAllowedCharge(ChargingRequestDto chargingRequestDto)
	{
		Double allowedChrg=0.0;
		
		
		logger.info("cp id"+chargingRequestDto.getChargePointId());
		logger.info("cp conne id"+chargingRequestDto.getConnectorId());
		logger.info("cp getRequestAmount"+chargingRequestDto.getRequestAmount());   

		//ChargingPointConnectorRateDto	chargingPointConnectorRateDto = chargingPointConnectorRateService.getConnectorByChargingPointNameAndConnectorIdAndAmount(chargingRequestEntity.getChargingPointEntity().getChargingPointId(),chargingRequestEntity.getConnectorEntity().getConnectorId(),chargingRequestEntity.getRequestAmount());
		
		ChargingPointConnectorRateDto	chargingPointConnectorRateDto = chargingPointConnectorRateService.getConnectorByChargingPointNameAndConnectorIdAndAmount(chargingRequestDto.getChargePointId(),Integer.toString(chargingRequestDto.getConnectorId()),Double.parseDouble(chargingRequestDto.getRequestAmount()));
		
		if(chargingPointConnectorRateDto==null)
		{
			throw BRSException.throwException("Error: NO Rate present for chargepoint connector"); 
		}
		
		if(chargingPointConnectorRateDto!=null)
		allowedChrg=chargingPointConnectorRateDto.getKWh();
		
		return allowedChrg;
	}
	
	public String callResetConnectorAPI(ChargingRequestEntity chargingRequestEntity)
	{
		URL getUrl = null;
	    String strResponse = null;
		try 
		{
			getUrl = new URL(StaticPathContUtils.SERVER_API_URL+"ResetConnector/"+chargingRequestEntity.getChargingPointEntity().getChargingPointId()+"/"+chargingRequestEntity.getConnectorEntity().getConnectorId());
			
		//	getUrl = new URL("http://125.99.153.126:8080/API/ResetConnector/TACW2242321G0285/1");
			
			HttpURLConnection conection;
			conection = (HttpURLConnection) getUrl.openConnection();
			conection.setRequestMethod("GET");
			
			logger.info("Resetting gChargingPointId="+chargingRequestEntity.getChargingPointEntity().getChargingPointId());
			logger.info("Resetting ConnectorId="+chargingRequestEntity.getConnectorEntity().getConnectorId());
	     
	        logger.info("ResetConnector ResponseMessage="+conection.getResponseMessage());
	        logger.info("ResetConnector responseCode="+conection.getResponseCode());
	       
	        BufferedReader br = null;
	        br = new BufferedReader(new InputStreamReader((conection.getInputStream())));

	        if (conection.getResponseCode()==200 && conection.getResponseMessage().equals("OK") ) {
	        	
	        	
	            br = new BufferedReader(new InputStreamReader(conection.getInputStream()));
	        } else {
	        	
				throw BRSException.throwException("Error: in ResetConnector API call"); 
	        }
	        
	        logger.info("ResetConnector br="+br.toString());
	        StringBuilder response = new StringBuilder();
	        
	        while ((strResponse = br.readLine()) != null) 
	            response.append(strResponse);
	        
	        
	        
	        final ObjectMapper mapper = new ObjectMapper();
	        JsonNode actualObj = mapper.readTree(response.toString());
	        
	        logger.info("response111="+actualObj.get("Status").textValue());
	        logger.info("response222="+actualObj.get("Payload").textValue());
	        
	        
	        if((actualObj.get("Status").textValue()).equals("OK"))
	        {
	        	strResponse="OK";
	        }
	        else if((actualObj.get("Status").textValue()).equals("Error"))
	        {
				throw BRSException.throwException("Error: in ResetConnector API call "+actualObj.get("Payload")); 
	        }
	        else
	        {
				throw BRSException.throwException("Error: in ResetConnector API call "); 
	        }
	        
	        br.close();
	        logger.info("ResetConnector response message="+response);
	        
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	        logger.info("Error: ResetConnector API="+e.toString());
			throw BRSException.throwException("Error: ResetConnector API"+e.toString()); 

		}

        // Getting response code
		return strResponse;
		
	}
	
	public String callHardResetConnectorAPI(ChargingRequestDto chargingRequestDto)
	{
		URL getUrl = null;
	    String strResponse = null;
		try 
		{
			getUrl = new URL(StaticPathContUtils.SERVER_API_URL+"HardChargerReset/"+chargingRequestDto.getChargePointId());
			
		//	getUrl = new URL("http://125.99.153.126:8080/API/ResetConnector/TACW2242321G0285/1");
			
			HttpURLConnection conection;
			conection = (HttpURLConnection) getUrl.openConnection();
			conection.setRequestMethod("GET");
			
			logger.info("Resetting gChargingPointId="+chargingRequestDto.getChargePointId());
			logger.info("Resetting ConnectorId="+chargingRequestDto.getChargePointId());
	     
	        logger.info("HardChargerReset ResponseMessage="+conection.getResponseMessage());
	        logger.info("HardChargerReset responseCode="+conection.getResponseCode());
	       
	        BufferedReader br = null;
	        br = new BufferedReader(new InputStreamReader((conection.getInputStream())));

	        if (conection.getResponseCode()==200 && conection.getResponseMessage().equals("OK") ) {
	        	
	        	
	            br = new BufferedReader(new InputStreamReader(conection.getInputStream()));
	        } else {
	        	
				throw BRSException.throwException("Error: in HardChargerReset API call"); 
	        }
	        
	        logger.info("HardChargerReset br="+br.toString());
	        StringBuilder response = new StringBuilder();
	        
	        while ((strResponse = br.readLine()) != null) 
	            response.append(strResponse);
	        
	        
	        
	        final ObjectMapper mapper = new ObjectMapper();
	        JsonNode actualObj = mapper.readTree(response.toString());
	        
	        logger.info("HardChargerResetresponse111="+actualObj.get("Status").textValue());
	       // logger.info("HardChargerResetresponse222="+actualObj.get("Payload").textValue());
	        
	        
	        if((actualObj.get("Status").textValue()).equals("OK"))
	        {
	        	strResponse="OK";
	        }
	        else if((actualObj.get("Status").textValue()).equals("Error"))
	        {
				throw BRSException.throwException("Error: in HardChargerReset API call "+actualObj.get("Payload").textValue()); 
	        }
	        else
	        {
				throw BRSException.throwException("Error: in HardChargerReset API call "); 
	        }
	        
	        br.close();
	        logger.info("HardChargerReset response message="+strResponse);
	        
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	        logger.info("Error: HardChargerReset API="+e.toString());
			throw BRSException.throwException("Error: HardChargerReset API"+e.toString()); 

		}

        // Getting response code
		return strResponse;
		
	}
	
	@Override
	public List<ChargingRequestRespDto> getChargingHistoryDetailsByStation(ChargingStationWiseReportIncomingDto chargingStationWiseReportIncomingDto) {
		// TODO Auto-generated method stub

		// String connectorId= Integer.toString(chargingRequestDto.getConnectorId());

		if (chargingStationWiseReportIncomingDto.getChargePointId() == null) {
			throw BRSException.throwException("Error : Charge Point ID cannot be blank");
		}
		ChargingPointEntity chargingPointEntity = chargingPointService.getChargingPointEntityByChargingPointId(chargingStationWiseReportIncomingDto.getChargePointId());
//		ChargingRequestEntity entity = chargingRequestEntity.get();

		if (chargingPointEntity == null) {
			throw BRSException.throwException("Error: No Charging Point present");
		}
		
		System.out.println("CpId======"+chargingPointEntity.getId()+"SDate=="+chargingStationWiseReportIncomingDto.getStartDate()+"EDate==="+chargingStationWiseReportIncomingDto.getEndDate());
		
		
		List<ChargingRequestEntity> chargingRequestEntities = chargingRequestRepository.findByChargingPointEntity(chargingPointEntity.getId(), chargingStationWiseReportIncomingDto.getStartDate(), chargingStationWiseReportIncomingDto.getEndDate());
		
	//	ChargingRequestMapper.toChargingRequestRespDtos(chargingRequestEntities);
		
	//	 ChargingRequestEntity chargingRequestEntity = chargingRequestRepository.findByChargingPointEntityAndConnectorEntityAndStatus();
	//	 chargingRequestEntity=chargingRequestRepository.findAll(chargingPointEntity);
		  
	//	 chargingRequestEntity.getId();
		 

	//	ChargingRequestMapper.toChargingRequestRespDtos(chargingRequestEntities);
		return ChargingRequestMapper.toChargingRequestRespDtos(chargingRequestEntities);  
	
	}

	@Override
	public int getYesterdayConsumedKwh() {
		// TODO Auto-generated method stub
		return chargingRequestRepository.getYesterdayConsumedKwh();
	}

	@Override
	public int getWeekConsumedKwh() {
		// TODO Auto-generated method stub
		return chargingRequestRepository.getWeekConsumedKwh();
	} 
	
	@Override
	public String get30daysTotalChargingTime() {
		// TODO Auto-generated method stub
		return chargingRequestRepository.get30daysTotalChargingTime();
	} 
	
	@Override
	public int weekTotalChargingRequestCountSessions() {
		// TODO Auto-generated method stub
		return chargingRequestRepository.weekTotalChargingRequestCountSessions();
	} 
	
	@Override
	public List<MostActiveChargingStationsDto> getMostActiveChargingStations() {
		// TODO Auto-generated method stub
		ObjectMapper mapper = new ObjectMapper();
		
		List<MostActiveChargingStationsDto> activeChargingStationsDtos = new ArrayList<MostActiveChargingStationsDto>();
		
		List<Map<String, String>>  list =  chargingRequestRepository.getMostActiveChargingStations();
		
		for (int i = 0; i < list.size(); i++) 
		{
		
			activeChargingStationsDtos.add(mapper.convertValue(list.get(i), MostActiveChargingStationsDto.class));

		}		
		
		return activeChargingStationsDtos ; 
	} 
	
	
	@Override
	public UserDashboardDto getUserChargingRequestDetails(String id) {
		// TODO Auto-generated method stub
		ObjectMapper mapper = new ObjectMapper();
		
		Map<String, String>  userDashboardMap =  chargingRequestRepository.getUserChargingRequestDetails(id);
		
		
		UserDashboardDto userDashboardDto = mapper.convertValue(userDashboardMap, UserDashboardDto.class);
		
		return userDashboardDto ; 
	}

	@Override
	public ChargingRequestEntity getRecentReharge(String id) {
		// TODO Auto-generated method stub
		return chargingRequestRepository.getRecentReharge(id);
	}

// 	
	
	@Override
	public int getYesterdayConsumedKwhByPartnerId(UserInfoEntity userInfoEntity) {
		// TODO Auto-generated method stub
		return chargingRequestRepository.getYesterdayConsumedKwhByPartnerId(userInfoEntity.getPartner().getId());
	}

	@Override
	public int getWeekConsumedKwhByPartnerId(UserInfoEntity userInfoEntity) {
		// TODO Auto-generated method stub
		return chargingRequestRepository.getWeekConsumedKwhByPartnerId(userInfoEntity.getPartner().getId());
	}

	@Override
	public String get30daysTotalChargingTimeByPartnerId(UserInfoEntity userInfoEntity) {
		// TODO Auto-generated method stub
		return chargingRequestRepository.get30daysTotalChargingTimeByPartnerId(userInfoEntity.getPartner().getId());
	}

	@Override
	public int weekTotalChargingRequestCountSessionsByPartnerId(UserInfoEntity userInfoEntity) {
		// TODO Auto-generated method stub
		return chargingRequestRepository.weekTotalChargingRequestCountSessionsByPartnerId(userInfoEntity.getPartner().getId());
	}

	@Override
	public List<MostActiveChargingStationsDto> getMostActiveChargingStationsByPartnerId(UserInfoEntity userInfoEntity) {
		// TODO Auto-generated method stub
		ObjectMapper mapper = new ObjectMapper();
		
		List<MostActiveChargingStationsDto> activeChargingStationsDtos = new ArrayList<MostActiveChargingStationsDto>();
		
		List<Map<String, String>>  list =  chargingRequestRepository.getMostActiveChargingStationsByPartnerId(userInfoEntity.getPartner().getId());
		
		for (int i = 0; i < list.size(); i++) 
		{
		
			activeChargingStationsDtos.add(mapper.convertValue(list.get(i), MostActiveChargingStationsDto.class));

		}		
		
		return activeChargingStationsDtos ; 
	} 
	
}
