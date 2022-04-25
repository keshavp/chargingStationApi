package com.scube.chargingstation.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scube.chargingstation.dto.ChargingPointDto;
import com.scube.chargingstation.dto.incoming.ChargingRequestDto;
import com.scube.chargingstation.dto.incoming.ChargingStationDto;
import com.scube.chargingstation.dto.mapper.ChargingPointMapper;
import com.scube.chargingstation.dto.response.Response;
import com.scube.chargingstation.entity.ChargingPointEntity;
import com.scube.chargingstation.repository.ChargerTypeRepository;
import com.scube.chargingstation.service.ChargingRequestService;
import com.scube.chargingstation.util.FileStorageService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = {"/api/v1/chargingstation"}, produces = APPLICATION_JSON_VALUE)
public class ChargingStationController {

	
	private static final Logger logger = LoggerFactory.getLogger(ChargingStationController.class);
	
	@Autowired 
	ChargingRequestService chargingRequestService;
	
	@Autowired
	ChargerTypeRepository chargerTypeRepository;
	
	@Autowired
	FileStorageService fileStorageService;
	
	
	@PostMapping("/getNearByChargingStationsDummy")
	public Map<String, Object> getNearByChargingStations11(@RequestBody ChargingStationDto hargingStationDto) throws Exception {
	
	Map<String, Object> map = new HashMap<String, Object>();
	
	Map<String, Object> jsonObjectData = null; 
	Map<String, Object> jsonCtypeData = null; 

	
	List<Map<String, Object>> jsonArrayData = new ArrayList<Map<String, Object>>();

	List<Map<String, Object>> mapCtypeData = new ArrayList<Map<String, Object>>();

	
	
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
	 
	 
	 for (int i=1;i<4;i++)
	 {
	 		jsonCtypeData = new HashMap<String, Object>();

	 		if(i==1)
	 		{
	 			String path="C:/var/lib/tomcat9/webapps/chargerImages/CCS2.png";
	 			jsonCtypeData.put("imagePath", path);
	 			jsonCtypeData.put("name", "CCS2");			
	 			jsonCtypeData.put("connectorId", "1");
	 				
	 			
	 		}
	 		else if(i==2)
	 		{
	 			jsonCtypeData.put("imagePath", "C:/var/lib/tomcat9/webapps/chargerImages/GBT.png");
	 			jsonCtypeData.put("name", "GB/T");			
	 			jsonCtypeData.put("connectorId", "1");
	 		}
	 		else if(i==3)
	 		{
	 			jsonCtypeData.put("imagePath", "C:/var/lib/tomcat9/webapps/chargerImages/CHAdeMo.png");
	 			jsonCtypeData.put("name", "CHAdeMo");			
	 			jsonCtypeData.put("connectorId", "1");
	 		}
	 		
	 	
	 		
	 		mapCtypeData.add(jsonCtypeData);

	 	}
	
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
		jsonObjectData.put("chargingPointId", "TACW2242321G0285");


		jsonObjectData.put("chargertypes", ctlist);//ctlist  //mapCtypeData
		jsonObjectData.put("amenties", amenties);
		
		jsonArrayData.add(jsonObjectData);

	}
	map.put("data", jsonArrayData);
	
	return map;
	
	}
	

	
	@PostMapping( value = "/addChargingRequest" , consumes = APPLICATION_JSON_VALUE)
	public Response addChargingRequest(@Valid @RequestBody ChargingRequestDto chargingRequestDto) {
		logger.info("***addChargingRequest***");
		
				return Response.ok().setPayload(chargingRequestService.addChargingRequest(chargingRequestDto));
		
	}
	
		
	
	@PostMapping(value ="/getNearByChargingStations", consumes = APPLICATION_JSON_VALUE)
	public Response getNearByChargingStations(@RequestBody ChargingStationDto chargingStationDto) throws Exception {
		
		
		return Response.ok().setPayload(chargingRequestService.getNearByChargingStations(chargingStationDto));
	
	}
	
	
	@PostMapping(value ="/getChargingHistory", consumes = APPLICATION_JSON_VALUE)
	public List<ChargingPointDto> getChargingHistory(ChargingStationDto chargingStationDto) {
		// TODO Auto-generated method stub
		
		//List<ChargingPointEntity> cpEntity=chargingPointRepository.findAll();
	//	List<ChargingPointDto> chargingPointDto = ChargingPointMapper.toChargingPointDto(cpEntity);
		
		return null;
		
	}
	 

	 
	 
}