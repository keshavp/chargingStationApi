package com.scube.chargingstation.dto.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scube.chargingstation.dto.CheckAllChargerStatusDto;

public class CheckChargerStatusMapper {
	
	private static final Logger logger = LoggerFactory.getLogger(CheckChargerStatusMapper.class);
	
	public static List<CheckAllChargerStatusDto> toCheckAllChargerStatusDto(List<Map<String, String>> chargerStatusList) {
		
		final ObjectMapper mapper = new ObjectMapper();
		List<CheckAllChargerStatusDto> chargerStatusDtosList = new ArrayList<CheckAllChargerStatusDto>();
		
		for(int i=0; i<chargerStatusList.size(); i++) {
			
			CheckAllChargerStatusDto checkAllChargerStatusDto = new CheckAllChargerStatusDto();
			final CheckAllChargerStatusDto pojo = mapper.convertValue(chargerStatusList.get(i), CheckAllChargerStatusDto.class);
			
			String[] issueReportedDateList = pojo.getCreated_at_update().split(",");
			logger.info("Issue Reported Date List :- " + issueReportedDateList.length);
			
			int index = issueReportedDateList.length;
			logger.info("Index of String Array" + index);
			
			String checkStartHour = issueReportedDateList[0].substring(11,13);
			logger.info("Check Start Time Hour " + checkStartHour);
			
			String checkEndHour = issueReportedDateList[index-1].substring(11,13);
			logger.info("Check End Time Hour " + checkEndHour);
			
			String amPMFilter = "";
			
			if(Integer.valueOf(checkStartHour) > 12 && Integer.valueOf(checkEndHour) > 12) {
				amPMFilter = "PM";
			}
			else {
				amPMFilter = "AM";
			}
			
			String startDateIndexObj = issueReportedDateList[0].substring(0,19);
			logger.info("Start Date :-- " + startDateIndexObj);
			
			String endDateIndexObj = issueReportedDateList[index-1].substring(0,19);
			logger.info("Last Date :-- " + endDateIndexObj);
			
			checkAllChargerStatusDto.setIssueReportedDateTime(endDateIndexObj + " " +  amPMFilter + " - " + startDateIndexObj + " "  + amPMFilter);
			checkAllChargerStatusDto.setChargingPointId(pojo.getCharging_point_id());
			checkAllChargerStatusDto.setChargingPointName(pojo.getCharging_point_name());
			checkAllChargerStatusDto.setChargingPointStatus(pojo.getCharging_point_status());
			checkAllChargerStatusDto.setConnectorId(pojo.getConnector_id());
			checkAllChargerStatusDto.setConnectorStatus(pojo.getConnector_status());
			
			chargerStatusDtosList.add(checkAllChargerStatusDto);
		}
	
		return chargerStatusDtosList;
	}

}
