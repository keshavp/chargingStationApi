package com.scube.chargingstation.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scube.chargingstation.controller.ChargingRequestController;
import com.scube.chargingstation.dto.ChargingPointDto;
import com.scube.chargingstation.dto.incoming.ChargingRequestDto;
import com.scube.chargingstation.entity.ChargingRequestEntity;
import com.scube.chargingstation.exception.BRSException;

public class CheckChargerStatus {

	
	private static final Logger logger = LoggerFactory.getLogger(CheckChargerStatus.class);

	
	public static String callResetConnectorApi(String CpId,String connectorId)
	{
		URL getUrl = null;
	    String strResponse = null;
		try 
		{
			getUrl = new URL(StaticPathContUtils.SERVER_API_URL+"ResetConnector/"+CpId+"/"+connectorId);
			
		//	getUrl = new URL("http://125.99.153.126:8080/API/ResetConnector/TACW2242321G0285/1");
			
			HttpURLConnection conection;
			conection = (HttpURLConnection) getUrl.openConnection();
			conection.setRequestMethod("GET");
			
			logger.info("Resetting gChargingPointId="+CpId);
			logger.info("Resetting ConnectorId="+connectorId);
	     
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
	        
	        String chkJsonformat=response.toString();
	        JsonNode actualObj=null;
			if(chkJsonformat.contains("chargepoint"))
			{
				logger.info("No live chargers found");
				strResponse="Not ok";
				return strResponse;
			}
			else
			{
	        
	        final ObjectMapper mapper = new ObjectMapper();
	        
	        actualObj = mapper.readTree(response.toString());
	        
	        logger.info("response111="+actualObj.get("Status").textValue());
	        logger.info("response222="+actualObj.get("Payload").textValue());
			}
	        
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
	
	
	public static String callChangeConfiguration(String CpId)
	{
		URL getUrl = null;
	    String strResponse = null;
		try 
		{
			getUrl = new URL(StaticPathContUtils.SERVER_API_URL+"ChangeConfiguration/"+CpId+"/MeterValueSampleInterval/10");
			
		//	getUrl = new URL("http://125.99.153.126:8080/API/ResetConnector/TACW2242321G0285/1");
			
			HttpURLConnection conection;
			conection = (HttpURLConnection) getUrl.openConnection();
			conection.setRequestMethod("GET");
			
			logger.info("callChangeConfiguration for="+CpId);
	     
	        logger.info("callChangeConfiguration ResponseMessage="+conection.getResponseMessage());
	        logger.info("callChangeConfiguration responseCode="+conection.getResponseCode());
	       
	        BufferedReader br = null;
	        br = new BufferedReader(new InputStreamReader((conection.getInputStream())));

	        if (conection.getResponseCode()==200 && conection.getResponseMessage().equals("OK") ) {
	        	
	        	
	            br = new BufferedReader(new InputStreamReader(conection.getInputStream()));
	        } else {
	        	
				throw BRSException.throwException("Error: in callChangeConfiguration API call"); 
	        }
	        
	        logger.info("callChangeConfiguration br="+br.toString());
	        StringBuilder response = new StringBuilder();
	        
	        while ((strResponse = br.readLine()) != null) 
	            response.append(strResponse);
	        
	        String chkJsonformat=response.toString();
	        JsonNode actualObj=null;
			if(chkJsonformat.contains("Chargepoint"))
			{
				logger.info("No live chargers found");
				strResponse="Not ok";
				return strResponse;
			}
			else
			{
	        
	        final ObjectMapper mapper = new ObjectMapper();
	        
	        actualObj = mapper.readTree(response.toString());
	        
	        logger.info("response111="+actualObj.get("Status").textValue());
	        logger.info("response222="+actualObj.get("Payload").textValue());
			}
	        
	        if((actualObj.get("Status").textValue()).equals("OK"))
	        {
	        	strResponse="OK";
	        }
	        else if((actualObj.get("Status").textValue()).equals("Error"))
	        {
				throw BRSException.throwException("Error: in callChangeConfiguration API call "+actualObj.get("Payload")); 
	        }
	        else
	        {
				throw BRSException.throwException("Error: in callChangeConfiguration API call "); 
	        }
	        
	        br.close();
	        logger.info("callChangeConfiguration response message="+response);
	        
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	        logger.info("Error: callChangeConfiguration API="+e.toString());
			throw BRSException.throwException("Error: callChangeConfiguration API"+e.toString()); 

		}

        // Getting response code
		return strResponse;
		
	}
	
	public static boolean callRemoteStopAPI(String ChargePointId,int transId) 
	{
		URL getUrl = null;
		//Double allowdChrg=0.0;
	    String strResponse = null;
		try 
		{
		//	allowdChrg=getAllowedCharge(chargingRequestDto);
			getUrl = new URL(StaticPathContUtils.SERVER_API_URL+"RemoteStop/"+ChargePointId+"/"+transId);
		//	getUrl = new URL("http://125.99.153.126:8080/API/RemoteStart/1347212300231/1/.05");
		//	getUrl = new URL("http://125.99.153.126:8080/API/RemoteStart/TACW2242321G0285/1/.05");
			logger.info("callRemoteStopAPI"+getUrl);
			
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
	        	
				throw BRSException.throwException("Error: in callRemoteStopAPI call "+conection.getResponseCode()); 
	        }
	        
	        logger.info("br="+br.toString());
	        StringBuilder response = new StringBuilder();
	        
	        while ((strResponse = br.readLine()) != null) 
	            response.append(strResponse);
	        
	        String chkJsonformat=response.toString();
	        JsonNode actualObj=null;
			if(chkJsonformat.contains("Chargepoint"))
			{
				logger.info("Chargepoint is not connected. Check chargepoint");
				//strResponse="Not ok";
				return false;
			}
	        
	        br.close();
	        logger.info("API response string="+response);
	        
	        final ObjectMapper mapper = new ObjectMapper();
	        actualObj = mapper.readTree(response.toString());
	        
	        logger.info("response111="+actualObj.get("Status").textValue());
	        logger.info("response222="+actualObj.get("Payload").textValue());
	        
	        
	        if((actualObj.get("Status").textValue()).equals("OK"))
	        {
	        	return true;
	        }
	        else if((actualObj.get("Status").textValue()).equals("Error"))
	        {
				
				/*
				 * strResponse=actualObj.get("Payload").textValue();
				 * 
				 * 
				 * if(strResponse.contains("running")) { strResponse = "Running"; } else
				 * if(strResponse.contains("offline")) { strResponse = "Offline"; } else { throw
				 * BRSException.throwException("Error: in callRemoteStopAPI API call "+actualObj
				 * .get("Payload"));
				 * 
				 * }
				 */
	        	//if already running transaction
	        	
	        	//if offline
	        	
	        	return false;
				
	        }
	        else
	        {
	        	
	        	return false;
				//throw BRSException.throwException("Error: in callRemoteStopAPI API call "); 
	        }
	        
		} 
	
	  catch (IOException e) { // TODO Auto-generated catch block e.printStackTrace();
		  		throw BRSException.throwException("Error: callRemoteStopAPI API");
	  
	  }
	 

        // Getting response code
	//	return true;
		
	}
	
	
	public static JsonNode callAllChargerStatusAPI(ChargingPointDto chargingPointDto)
	{
		URL getUrl = null;
	    String strResponse = null;
	    JsonNode respNode=null;
		try 
		{
			getUrl = new URL(StaticPathContUtils.SERVER_API_URL+"AllChargersStatus/"+chargingPointDto.getChargingPointId());
			
		//	getUrl = new URL("http://evdock.app:8082/API/AllChargersStatus/"+chargingPointDto.getChargingPointId());
		//	getUrl = new URL("http://125.99.153.126:8080/API/AllChargersStatus/"+chargingPointDto.getChargingPointId());
			
			HttpURLConnection conection;
			conection = (HttpURLConnection) getUrl.openConnection();
			conection.setRequestMethod("GET");
			
	        logger.info("AllChargersStatus ResponseMessage="+conection.getResponseMessage());
	        logger.info("AllChargersStatus responseCode="+conection.getResponseCode());
	       
	        BufferedReader br = null;
	        br = new BufferedReader(new InputStreamReader((conection.getInputStream())));

	        if (conection.getResponseCode()==200 && conection.getResponseMessage().equals("OK") ) {
	        	
	            br = new BufferedReader(new InputStreamReader(conection.getInputStream()));
	        } else {
	        	
				throw BRSException.throwException("Error: in AllChargersStatus API call"); 
	        }
	        
	        logger.info("AllChargersStatus br="+br.toString());
	        StringBuilder response = new StringBuilder();
	        
	        while ((strResponse = br.readLine()) != null) 
	            response.append(strResponse);
	        
	        final ObjectMapper mapper = new ObjectMapper();
	        JsonNode actualObj = mapper.readTree(response.toString());
	        
	        logger.info("AllChargersStatus response111="+actualObj.get("Status").textValue());
	        logger.info("AllChargersStatus response222="+actualObj.get("Payload").textValue());
	      
	        
	        if((actualObj.get("Status").textValue()).equals("OK"))
	        {
				
				final ObjectMapper mapper1 = new ObjectMapper();
				JsonNode nodePayload=null;
				
				String chkJsonformat=actualObj.get("Payload").textValue();
				
				if(chkJsonformat.equals("No live chargers found"))
				{
					logger.info("No live chargers found");
				}
				else
				{
				 nodePayload = mapper1.readTree(actualObj.get("Payload").textValue());
				}
				
				if(nodePayload == null || nodePayload.isNull())
				{}
				else
				{
			 	final ObjectMapper mapper3 = new ObjectMapper();
		 	    JsonNode nodeCnter = mapper3.readTree(nodePayload.get(0).toString());
				 	   if(nodeCnter == null || nodeCnter.isNull())
				 	   {
				 		   
				 	   }
				 	   else
				 	   {
				 	    logger.info("OnlineConnectors=at 3"+ nodeCnter.get("OnlineConnectors"));
				 	    logger.info("connector status is"+ nodeCnter.get("status"));
				 	    String constatus=nodeCnter.get("status").textValue();
				 	    
				 	    logger.info("constatus"+constatus);

				 	    
				 	    if(constatus.equals("Open"))
				 	    {
					 	    logger.info("here");

				 	    final ObjectMapper objectMapper = new ObjectMapper(); 
				 	    respNode = objectMapper.readTree(nodeCnter.get("OnlineConnectors").toString());
				 	    logger.info("here respNode"+respNode);

				 	    }
				 	    //close will return empty respNode
				 	    
				 	   }
				}
		 	    	
	        }
	        else if((actualObj.get("Status").textValue()).equals("Error"))
	        {
				throw BRSException.throwException("Error: in AllChargersStatus API call "+actualObj.get("Payload")); 
	        }
	        else
	        {
				throw BRSException.throwException("Error: in AllChargersStatus API call "); 
	        }
	        
	        br.close();
	        logger.info("AllChargersStatus response node="+respNode);
	        
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	        logger.info("Error: AllChargersStatus API="+e.toString());
			throw BRSException.throwException("Error: AllChargersStatus API"+e.toString()); 

		}

        // Getting response code
		return respNode; 
		
	}
	
	public static String callRemoteStartAPI(ChargingRequestDto chargingRequestDto,Double allowdChrg) 
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
			logger.info("callRemoteStartAPI"+getUrl);
			
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
	
	public static String callResetConnectorAPI(ChargingRequestEntity chargingRequestEntity)
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
	
	
	
	public static String callHardResetConnectorAPI(ChargingRequestDto chargingRequestDto)
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
	
}
