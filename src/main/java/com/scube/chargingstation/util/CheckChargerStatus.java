package com.scube.chargingstation.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scube.chargingstation.controller.ChargingRequestController;
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
}
