package com.scube.chargingstation.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scube.chargingstation.dto.incoming.ChargingStationDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = {"/api/v1/chargingstation"}, produces = APPLICATION_JSON_VALUE)
public class ChargingStationController {

	
	private static final Logger logger = LoggerFactory.getLogger(ChargingStationController.class);
	
	
	@PostMapping("/getNearByChargingStations")
	public Map<String, Object> getNearByChargingStations(@RequestBody ChargingStationDto hargingStationDto) throws Exception {
	
	Map<String, Object> map = new HashMap<String, Object>();
	
	Map<String, Object> jsonObjectData = null; 
	List<Map<String, Object>> jsonArrayData = new ArrayList<Map<String, Object>>();

	 ArrayList<String> ctlist=new ArrayList<String>();//Creating arraylist    
	 ctlist.add("CCS2");//Adding object in arraylist    
	 ctlist.add("GB/T");    
	 ctlist.add("CHAdeMo");    
	
	 ArrayList<String> amenties=new ArrayList<String>();//Creating arraylist    
	 amenties.add("Restaurant");//Adding object in arraylist    
	 amenties.add("Medical Shop");    
	 amenties.add("Washrooms");    
	 amenties.add("Cafe");  
	 String address="";
	
	for (int i=1;i<5;i++)
	{
		jsonObjectData = new HashMap<String, Object>();

		if(i==1)
		{
			address="Nitin company Thane";
			jsonObjectData.put("latitude", "19.198797");
			jsonObjectData.put("logitude", "72.965305");			
			jsonObjectData.put("name", "Nitin");
			jsonObjectData.put("rating", "3");
			jsonObjectData.put("status", "Open");
			jsonObjectData.put("distance", "2");
			
			
			
		}
		else if(i==2)
		{
			address="Kapur Bavdi Thane";
			jsonObjectData.put("latitude", "19.220113");
			jsonObjectData.put("logitude", "72.974845");			
			jsonObjectData.put("name", "Kapur");
			jsonObjectData.put("rating", "5");
			jsonObjectData.put("status", "Open");
			jsonObjectData.put("distance", "4");
		}
		else if(i==3)
		{
			address="Tikuji-ni-Wadi, Chitalsar, Thane West";
			jsonObjectData.put("latitude", "19.237988");
			jsonObjectData.put("logitude", "72.966878");
			jsonObjectData.put("name", "Chitalsar");
			jsonObjectData.put("rating", "3");
			jsonObjectData.put("status", "Close");
			jsonObjectData.put("distance", "1");
		}
		else if(i==4)
		{
			address="Kasarvadavali, Thane West";
			jsonObjectData.put("latitude", 	"19.267938");
			jsonObjectData.put("logitude","72.971154");
			jsonObjectData.put("name", "Kasarvadavali");
			jsonObjectData.put("rating", "1");
			jsonObjectData.put("status", "Open");
			jsonObjectData.put("distance", "5");
		}
		
		//String address="loreum ipsum test address"+i;
		
		jsonObjectData.put("id", ""+i);
		
		jsonObjectData.put("address", address);

		jsonObjectData.put("chargertypes", ctlist);
		jsonObjectData.put("amenties", amenties);
		
		jsonArrayData.add(jsonObjectData);

	}
	map.put("data", jsonArrayData);
	
	return map;
	
	}
	


	
}
