package com.scube.chargingstation.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scube.chargingstation.dto.CheckAllChargerStatusDto;
import com.scube.chargingstation.dto.incoming.ChargingRequestDto;
import com.scube.chargingstation.dto.incoming.ServerAPIResetIncomingDto;
import com.scube.chargingstation.dto.mapper.CheckChargerStatusMapper;
import com.scube.chargingstation.entity.ChargingPointEntity;
import com.scube.chargingstation.entity.ChargingRequestEntity;
import com.scube.chargingstation.entity.ConnectorEntity;
import com.scube.chargingstation.entity.SaveAllChargerStatusDataEntity;
import com.scube.chargingstation.exception.BRSException;
import com.scube.chargingstation.repository.ChargingRequestRepository;
import com.scube.chargingstation.repository.SaveAllChargerStatusDataRepository;
import com.scube.chargingstation.util.CheckChargerStatus;
import com.scube.chargingstation.util.DateUtils;
import com.scube.chargingstation.util.EmailService;

@Service
public class ServerAPIResetServiceImpl implements ServerAPIResetService {
	
	@Autowired
	ChargingPointService chargingPointService;
	
	@Autowired
	ConnectorService connectorService;
	
	@Autowired
	ChargingRequestRepository chargingRequestRepository;
	
	@Autowired
	SaveAllChargerStatusDataRepository saveAllChargerStatusDataRepository;
	
	@Autowired
	EmailService emailService;
	
	@Value("${charger.status.time}") private int chargerStatusTime;
	
	private static final Logger logger = LoggerFactory.getLogger(ServerAPIResetServiceImpl.class);

	@Override
	public boolean hardReset(@Valid ServerAPIResetIncomingDto serverAPIResetIncomingDto) {
		// TODO Auto-generated method stub
		
		logger.info("---------- ServerAPIResetServiceImpl hardReset -----------------");
		
		if(serverAPIResetIncomingDto.getChargingPointId() == "" || serverAPIResetIncomingDto.getChargingPointId().trim().isEmpty()) {
			throw BRSException.throwException("Error : Charging Point ID cannot be blank");
		}
/*		
		if(serverAPIResetIncomingDto.getConnectorId() == "" || serverAPIResetIncomingDto.getConnectorId().trim().isEmpty()) {
			throw BRSException.throwException("Error : Connector ID cannot be blank");
		}
*/		
		ChargingPointEntity chargingPointEntity = chargingPointService.getChargingPointEntityByChargePointId(serverAPIResetIncomingDto.getChargingPointId());
		
		if(chargingPointEntity == null) {
			throw BRSException.throwException("Error : Charging Point ID (" + serverAPIResetIncomingDto.getChargingPointId() + ") does not exist");
		}
/*		
		ConnectorEntity connectorEntity = connectorService.getConnectorEntityByConnectorIdAndChargingPointEntity(
				serverAPIResetIncomingDto.getConnectorId(), chargingPointEntity);
		
		if(connectorEntity == null) {
			throw BRSException.throwException("Error : Connector ID (" + serverAPIResetIncomingDto.getConnectorId() + ") does not exist");
		}
*/		
		ChargingRequestEntity chargingRequestEntity = chargingRequestRepository.findByChargingPointEntityAndChargingStatus(
				chargingPointEntity, "Starting");
		
		if(chargingRequestEntity != null) {
			throw BRSException.throwException("Error : Currently charging going on for Connector ID :-- (" + serverAPIResetIncomingDto.getConnectorId() + ") ");
		}
		
		ChargingRequestDto chargingRequestDto = new ChargingRequestDto();
		chargingRequestDto.setChargePointId(serverAPIResetIncomingDto.getChargingPointId());
	//	chargingRequestDto.setConnectorId(Integer.parseInt(serverAPIResetIncomingDto.getConnectorId()));
		
		logger.info("Charging Request Dto :--- " + chargingRequestDto);
		
		/*
		 * String response =
		 * CheckChargerStatus.callHardResetConnectorAPI(chargingRequestDto);
		 * 
		 * logger.info("Response for Hard Reset :---- " + response);
		 */
		
		logger.info("---- Hard Reset Successfully Completed -----");
		
		return true;
	}

	@Override
	public boolean softReset(@Valid ServerAPIResetIncomingDto serverAPIResetIncomingDto) {
		// TODO Auto-generated method stub
		
		logger.info("---------- ServerAPIResetServiceImpl softReset -----------------");
		
		if(serverAPIResetIncomingDto.getChargingPointId() == "" || serverAPIResetIncomingDto.getChargingPointId().trim().isEmpty()) {
			throw BRSException.throwException("Error : Charging Point ID cannot be blank");
		}
/*		
		if(serverAPIResetIncomingDto.getConnectorId() == "" || serverAPIResetIncomingDto.getConnectorId().trim().isEmpty()) {
			throw BRSException.throwException("Error : Connector ID cannot be blank");
		}
*/		
		ChargingPointEntity chargingPointEntity = chargingPointService.getChargingPointEntityByChargePointId(serverAPIResetIncomingDto.getChargingPointId());
		
		if(chargingPointEntity == null) {
			throw BRSException.throwException("Error : Charging Point ID (" + serverAPIResetIncomingDto.getChargingPointId() + ") does not exist");
		}
		
		List<ConnectorEntity> connectorEntityList = connectorService.getAllConnectorEntityListByChargingPointEntity(chargingPointEntity);
		
		if(connectorEntityList == null) {
			throw BRSException.throwException("Error : No Connectors Present for Charging Point ID " + chargingPointEntity.getChargingPointId());
		}
		
		logger.info("Connectors for Charging Point ID :-- " + serverAPIResetIncomingDto.getChargingPointId() + " are " +
				connectorEntityList.size());
		
		for(int i=0; i<connectorEntityList.size();i++) {
		
			ChargingRequestEntity chargingRequestEntity = chargingRequestRepository.findByChargingPointEntityAndConnectorEntityAndChargingStatus(
					chargingPointEntity, connectorEntityList.get(i), "Starting");
			
			if(chargingRequestEntity != null) {
				throw BRSException.throwException("Error : Currently charging going on for Connector ID :-- (" + serverAPIResetIncomingDto.getConnectorId() + ") ");
			}
			
			logger.info("---- No Charging going on currently on " + serverAPIResetIncomingDto.getChargingPointId() + 
					" on Connector :-- " + connectorEntityList.get(i).getConnectorId() + ". Processing with Soft Reset");
	/*		
			String response = CheckChargerStatus.callResetConnectorApi(chargingPointEntity.getChargingPointId(), connectorEntityList.get(i).getConnectorId());
			logger.info("Response for Soft Reset :---- " + response);
	*/	
		} 
		
		return true;
	}

	@Override
	public boolean sendEmailForChargerStatus() {
		// TODO Auto-generated method stub
		
		logger.info("---------- ServerAPIResetServiceImpl sendEmailForChargerStatus -----------------");
		
		try {
			
			JsonNode jsonNodeResponseData = CheckChargerStatus.callAllChargingStationAndChargerStatusApi();
			logger.info("------ Check API Status ------ " + jsonNodeResponseData.get("Status").textValue());
			logger.info("------ Check API Response Payload ------ " + jsonNodeResponseData.get("Payload").textValue());
	        
	        ObjectMapper objectMapper = new ObjectMapper();
	        
	        String emailContent = "";
	        String currentDate = "";
	        boolean sendMailFlag = false;
	        
	        if((jsonNodeResponseData.get("Status").textValue()).equals("OK")) {
	        	
				JsonNode jsonNodePayload=null;	
				jsonNodePayload = objectMapper.readTree(jsonNodeResponseData.get("Payload").textValue());
				
				if(jsonNodePayload != null) {
					
					logger.info("----- JSON Node Payload Size " + jsonNodePayload.size());
					
				 	JsonNode jsonNodeCenter = null;
			 	    
				 	for(int i=0; i<jsonNodePayload.size(); i++) {
				 		
				 		CheckAllChargerStatusDto checkAllChargerStatusDto = new CheckAllChargerStatusDto();
				 		
				 		jsonNodeCenter = objectMapper.readTree(jsonNodePayload.get(i).toString());
				 		logger.info("----- JSON Node Center " + jsonNodeCenter);
				 		
				 		if(jsonNodeCenter != null) {
				 			
				 			String chargingStationStatus = jsonNodeCenter.get("status").textValue();
				 			String chargingStationId = jsonNodeCenter.get("id").textValue();
				 			String chargingStationName = jsonNodeCenter.get("name").textValue();
				 			
				 			checkAllChargerStatusDto.setChargingPointId(chargingStationId);
				 			checkAllChargerStatusDto.setChargingPointName(chargingStationName);
				 			checkAllChargerStatusDto.setChargingPointStatus(chargingStationStatus);
				 			
				 			logger.info("Check All Charger Status Dto :- " + checkAllChargerStatusDto);
				 			
				 			JsonNode jsonOnlineConnectorNode = null;
				 			
				 			if(chargingStationStatus.equals("Open")) {
				 				
						 	    jsonOnlineConnectorNode = jsonNodeCenter.get("OnlineConnectors");
						 	    logger.info("----- jsonOnlineConnectorNode " + jsonOnlineConnectorNode);
						 	    logger.info("----- jsonOnlineConnectorNode Size " + jsonOnlineConnectorNode.size());				 	   
						 	  
						 	    List<String> onlineConnectorIdKey = new ArrayList<>(); 
						 	  
						 	    Iterator<String> iterator = jsonOnlineConnectorNode.fieldNames();
						 	    iterator.forEachRemaining(e -> onlineConnectorIdKey.add(e));
						 	    logger.info("---- Keys are " + onlineConnectorIdKey);
						 	  
						 	    for(int j=0; j<onlineConnectorIdKey.size(); j++) {
						 	    
						 	    	String connectorStatus = jsonOnlineConnectorNode.get(onlineConnectorIdKey.get(j)).get("Status").toString();
						 	    	logger.info("Connector Status :--- " + connectorStatus);
						 	      
						 	    	String connectorId = onlineConnectorIdKey.get(j);
						 	    	logger.info("Connector ID :--- " + connectorId);
						 	    	
						 	    	checkAllChargerStatusDto.setConnectorId(connectorId);
						 	    	
						 	    	if(connectorStatus.equals("0")) {
						 	    		sendMailFlag = true;
						 	    		checkAllChargerStatusDto.setConnectorStatus("Undefined");
						 	    	}
						 	    	else if(connectorStatus.equals("3")) {
						 	    		sendMailFlag = true;
						 	    		checkAllChargerStatusDto.setConnectorStatus("Unavailable");
						 	    	}
						 	    	else if(connectorStatus.equals("4")) {
						 	    		sendMailFlag = true;
						 	    		checkAllChargerStatusDto.setConnectorStatus("Faulted");
						 	    	}
						 	    	else {
						 	    		sendMailFlag = false;
						 	    		checkAllChargerStatusDto.setConnectorStatus("Hiii");
						 	    	}
						 	    	logger.info("Connector Status updated in Dto " + checkAllChargerStatusDto);
						 	    	
						 	    	if(connectorStatus.equals("0") || connectorStatus.equals("3") 
						 	    			|| connectorStatus.equals("4")) {
						 	    		
						 	    		currentDate = DateUtils.todayStrSimpleDateTimeFormat();
						 	    		logger.info("----- Current Date when issue is reported :- " + currentDate);
						 	    		emailContent += "Charging Station : " + checkAllChargerStatusDto.getChargingPointId() + " ( "+checkAllChargerStatusDto.getChargingPointName()+" ) <br>"
						 				  	+ "Connector ID : " + checkAllChargerStatusDto.getConnectorId() + "<br>"
						 				  	+ "Connector Status : " + checkAllChargerStatusDto.getConnectorStatus() + "<br>"
						 				  	+ "--------------------------------------------------------------------<br>";
						 	    	}
						 	    }
				 			}
				 		}
				 	}
				 	
				 	logger.info("------- Check Mail content -----" + emailContent);
				 	
			 		String thankYouContent = "Thank You, <br>";
			 		if(sendMailFlag == true) {
			 			emailService.sendEmailForAllChargerStatus(emailContent, thankYouContent, currentDate);
			 		}
				}
	        }
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}

	@Override
	public boolean saveAllChargerStatusData() {
		// TODO Auto-generated method stub
		
		logger.info("---------- ServerAPIResetServiceImpl saveAllChargerStatusData -----------------");
		
		try {
			JsonNode jsonResponseData = CheckChargerStatus.callAllChargingStationAndChargerStatusApi();
			logger.info("------ Check API Status ------ " + jsonResponseData.get("Status").textValue());
			logger.info("------ Check API Response Payload ------ " + jsonResponseData.get("Payload").textValue());
			
			ObjectMapper objectMapper = new ObjectMapper();
			
			if(jsonResponseData.get("Status").textValue().equals("OK")) {
				
				JsonNode jsonNodePayload=null;	
				jsonNodePayload = objectMapper.readTree(jsonResponseData.get("Payload").textValue());
				
				logger.info("----- JSON Node Payload Size " + jsonNodePayload.size());
				
				if(jsonResponseData != null) {
					
					logger.info("----- JSON Node Payload Size " + jsonNodePayload);
				 	
					JsonNode jsonNodeCenter = null;
					
				//	List<CheckAllChargerStatusDto> checkAllChargerStatusDtos = new ArrayList<CheckAllChargerStatusDto>();
					List<SaveAllChargerStatusDataEntity> savAllChargerStatuDataList = new ArrayList<SaveAllChargerStatusDataEntity>();
					
					for (int i = 0; i < jsonNodePayload.size(); i++) {
						
						jsonNodeCenter = objectMapper.readTree(jsonNodePayload.get(i).toString());
						logger.info("---- JSON Node Center ----" + jsonNodeCenter);
						logger.info("---- JSON Node Center Size ----" + jsonNodeCenter.size());
						
						if(jsonNodeCenter != null) {
				 			
				 			JsonNode jsonOnlineConnectorNode = null;
				 			
				 			if(jsonNodeCenter.get("status").textValue().equals("Open")) {
				 				
						 	    jsonOnlineConnectorNode = jsonNodeCenter.get("OnlineConnectors");
						 	    logger.info("----- JSON Online Connector Node " + jsonOnlineConnectorNode);
						 	    logger.info("----- Size of jsonOnlineConnectorNode" + jsonOnlineConnectorNode.size());
						 	    
						 	    List<String> onlineConnectorIdKey = new ArrayList<>(); 
							 	  
						 	    Iterator<String> iterator = jsonOnlineConnectorNode.fieldNames();
						 	    iterator.forEachRemaining(e -> onlineConnectorIdKey.add(e));
						 	    logger.info("---- Keys are " + onlineConnectorIdKey);
						 	    
						 	   for(int j=0; j<onlineConnectorIdKey.size(); j++) {
						 		   
						 	    	String connectorStatus = jsonOnlineConnectorNode.get(onlineConnectorIdKey.get(j)).get("Status").toString();
						 	    	logger.info("Connector Status :--- " + connectorStatus);
						 	      
						 	    	String connectorId = onlineConnectorIdKey.get(j);
						 	    	logger.info("Connector ID :--- " + connectorId);
						 	    	
									SaveAllChargerStatusDataEntity saveAllChargerStatusDataEntity = new SaveAllChargerStatusDataEntity();
						 	    //	CheckAllChargerStatusDto chargerStatusDto = new CheckAllChargerStatusDto();
						 	    	
						 	    	if(connectorStatus.equals("0")) {
						 	    		saveAllChargerStatusDataEntity.setConnectorID(onlineConnectorIdKey.get(j));
						 	    		saveAllChargerStatusDataEntity.setConnectorStatus("Undefined");
						 	    	}
						 	    	else if(connectorStatus.equals("3")) {
						 	    		saveAllChargerStatusDataEntity.setConnectorID(onlineConnectorIdKey.get(j));
						 	    		saveAllChargerStatusDataEntity.setConnectorStatus("Unavailable");
						 	    	}
						 	    	else if(connectorStatus.equals("4")) {
						 	    		saveAllChargerStatusDataEntity.setConnectorID(onlineConnectorIdKey.get(j));
						 	    		saveAllChargerStatusDataEntity.setConnectorStatus("Faulted");
						 	    	}
						 	    	
						 	    	if(connectorStatus.equals("0") || connectorStatus.equals("3") || connectorStatus.equals("4")) {
						 	    		
						 	    		saveAllChargerStatusDataEntity.setChargingPointId(jsonNodeCenter.get("id").textValue());
						 	    		saveAllChargerStatusDataEntity.setChargingPointName(jsonNodeCenter.get("name").textValue());
						 	    		saveAllChargerStatusDataEntity.setChargingPointStatus(jsonNodeCenter.get("status").textValue());
						 	    	
						 	    		SaveAllChargerStatusDataEntity checkDuplicateRecordsEntity = saveAllChargerStatusDataRepository.checkIfDuplicateRecordsInserted(
						 	    				jsonNodeCenter.get("id").textValue(), onlineConnectorIdKey.get(j));
						 	    		
						 	    		if(checkDuplicateRecordsEntity != null) {
						 	    			int currentCount = checkDuplicateRecordsEntity.getCount();
						 	    			long calculatedDifference = compareDateDifference(checkDuplicateRecordsEntity.getCreatedAt());
						 	    			
						 	    			logger.info("Current Count :--- " + currentCount + " Calcualted Difference :---- " + calculatedDifference);
						 	    			logger.info("----" + chargerStatusTime);
						 	    			
						 	    			if(calculatedDifference > chargerStatusTime) {
						 	    				int updatedCount = currentCount + 1;
						 	    				logger.info("Updated Count Now " + updatedCount);
						 	    				saveAllChargerStatusDataEntity.setCount(updatedCount);
						 	    			}
						 	    			else {
						 	    				saveAllChargerStatusDataEntity.setCount(currentCount);
						 	    			}
						 	    			
						 	    		}
						 	    		else {
						 	    			saveAllChargerStatusDataEntity.setCount(1);
						 	    		}
						 	    		
						 	    		savAllChargerStatuDataList.add(saveAllChargerStatusDataEntity);
						 	    	}
						 	   }
				 			}
						}
					}
					logger.info("Check All Dto Size " + savAllChargerStatuDataList.size());
					logger.info("Check All Dto " + savAllChargerStatuDataList);
					saveAllChargerStatusDataRepository.saveAll(savAllChargerStatuDataList);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return true;
	}
	
	
	public Long compareDateDifference(Instant insertedDate) {
		
		Date currentDate = new Date();
		Date insertedDateInDateFormat = Date.from(insertedDate);
		
		logger.info("Current Date ==> " + currentDate + "Inserted Date " + insertedDateInDateFormat);
		
		long currentDateInLong = currentDate.getTime();
		long insertedDateInLong = insertedDateInDateFormat.getTime();
		
		logger.info("Current Date In Long Format ==> " + currentDateInLong + " Inserted Date in Long " + insertedDateInLong);
		
		long differenceInTime = currentDate.getTime() - insertedDateInDateFormat.getTime();
		
		long differenceInMinutes = differenceInTime/(1000*60);
		
		logger.info("Difference in Current & Inserted Date ==> " + differenceInMinutes + "minutes");
		
		return differenceInMinutes;
	}

	@Override
	public List<CheckAllChargerStatusDto> getAllChargerStatusDataReportByDateRange(@Valid ServerAPIResetIncomingDto serverAPIResetIncomingDto) {
		// TODO Auto-generated method stub
		
		logger.info("---------- ServerAPIResetServiceImpl getAllChargerStatusDataReport -----------------");
		
		if(serverAPIResetIncomingDto.getStartDate() == null && serverAPIResetIncomingDto.getEndDate() == null) {
			throw BRSException.throwException("Error : Please Select your Date Range");
		}
		
		logger.info("---- Selected Date Range ----" + " Start Date ==> " + 
				serverAPIResetIncomingDto.getStartDate() + " End Date ==>  " + serverAPIResetIncomingDto.getEndDate());
		
		List<CheckAllChargerStatusDto> chargerStatusDtosList = new ArrayList<CheckAllChargerStatusDto>();
		
		if(serverAPIResetIncomingDto.getChargingPointId().equals("All")) {
			
			logger.info("---- Check Charger Status Report for All Charging Point ID ---- ");
			
			List<Map<String, String>> saveAllChargerStatusDataList = saveAllChargerStatusDataRepository.getAllChargerStatusDataByDateRange(
					serverAPIResetIncomingDto.getStartDate(), serverAPIResetIncomingDto.getEndDate());
			chargerStatusDtosList = CheckChargerStatusMapper.toCheckAllChargerStatusDto(saveAllChargerStatusDataList);
			
		}
		else {
			
			logger.info("---- Check Charger Status Report for Charging Point ID ---- " + serverAPIResetIncomingDto.getChargingPointId());
			
			List<Map<String, String>> saveAllChargerStatusDataList = saveAllChargerStatusDataRepository.getChargerStatusDataByChargingPointIdAndDateRange(
					 serverAPIResetIncomingDto.getChargingPointId(), serverAPIResetIncomingDto.getStartDate(), serverAPIResetIncomingDto.getEndDate());
			chargerStatusDtosList = CheckChargerStatusMapper.toCheckAllChargerStatusDto(saveAllChargerStatusDataList);
			
		}
		
		return chargerStatusDtosList;
	}
	
}
