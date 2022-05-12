package com.scube.chargingstation.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.scube.chargingstation.dto.AmenityDto;
import com.scube.chargingstation.dto.ChargingPointDto;
import com.scube.chargingstation.dto.ChargingStatusRespDto;
import com.scube.chargingstation.dto.ConnectorDto;
import com.scube.chargingstation.dto.incoming.ChargingRequestDto;
import com.scube.chargingstation.dto.incoming.ChargingStationDto;
import com.scube.chargingstation.dto.incoming.NotificationReqDto;
import com.scube.chargingstation.dto.mapper.AmenityMapper;
import com.scube.chargingstation.dto.mapper.ChargingPointMapper;
import com.scube.chargingstation.dto.mapper.ChargingStatusMapper;
import com.scube.chargingstation.dto.mapper.ConnectorMapper;
import com.scube.chargingstation.entity.AmenitiesEntity;
import com.scube.chargingstation.entity.CarModelEntity;
import com.scube.chargingstation.entity.ChargerTypeEntity;
import com.scube.chargingstation.entity.ChargingPointEntity;
import com.scube.chargingstation.entity.ChargingRequestEntity;
import com.scube.chargingstation.entity.ConnectorEntity;
import com.scube.chargingstation.entity.UserInfoEntity;
import com.scube.chargingstation.entity.UserWalletEntity;
import com.scube.chargingstation.exception.BRSException;
import com.scube.chargingstation.repository.CarModelRepository;
import com.scube.chargingstation.repository.ChargerTypeRepository;
import com.scube.chargingstation.repository.ChargingPointRepository;
import com.scube.chargingstation.repository.ChargingRequestRepository;
import com.scube.chargingstation.repository.UserInfoRepository;
import com.scube.chargingstation.repository.UserWalletDtlRepository;
import com.scube.chargingstation.repository.UserWalletRepository;
import com.scube.chargingstation.util.StaticPathContUtils;

@Service
public class ChargingRequestServiceImpl implements ChargingRequestService {

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
	
	@Autowired
	NotificationService notificationService;
	
	@Autowired
	CarModelRepository carModelRepository;
	
	
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
	  
	  List<ChargingPointEntity> cpEntityLst=chargingPointRepository.findAll();
	  
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
	  
	  List<ChargingPointEntity> cpEntityLst=chargingPointRepository.findAll();
	  List<ChargingPointDto> chargingPointDtoLst=new ArrayList<ChargingPointDto>();
	  
	  for(ChargingPointEntity chargingPointEntity : cpEntityLst) { ChargingPointDto
	  CPDto=new ChargingPointDto();
	  
	  Set<ConnectorDto> connectors=new HashSet<ConnectorDto>(); 
	  Set<AmenityDto> amenities =new HashSet<AmenityDto>();
	  
	  Set<ConnectorEntity> conEntitySet 	  =chargingPointEntity.getConnectorEntities(); Set<AmenitiesEntity>
	  ameEntitySet= chargingPointEntity.getAmenities();
	  
	  for(ConnectorEntity conEntity : conEntitySet) 
	  { 
		  ConnectorDto conDto=new  ConnectorDto(); 
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
					logger.info("***available***");
					conDto.setCompatible("Y");
			  }
			  else
			  {
					logger.info("***not available***");
					conDto.setCompatible("N");
			  }
		  }
	  conDto.setAvailable(conEntity.getConnectorStatusEntity().getLastStatus().equals("Available") ? "Y":"N");
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
	  CPDto.setDistance(chargingPointEntity.getDistance());
	  CPDto.setLatitude(chargingPointEntity.getLatitude());
	  CPDto.setLongitude(chargingPointEntity.getLongitude());
	  CPDto.setName(chargingPointEntity.getName());
	  CPDto.setRating(chargingPointEntity.getRating());
	  CPDto.setStatus(chargingPointEntity.getStatus());
	  CPDto.setChargingPointId(chargingPointEntity.getChargingPointId());
	  CPDto.setConnectors(connectors); CPDto.setAmenities(amenities);
	  
	  
	  chargingPointDtoLst.add(CPDto);
	  
	  }
	  
	  
	  
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
				notificationService.sendNotification(notificationReqDto);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				logger.info("***exception in sendGunInsertNotification***"+e.toString());
			}

			
		}
		
		timeoutPendingChargingRequests();
		
		
	}
	

	
	
}
