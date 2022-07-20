package com.scube.chargingstation.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scube.chargingstation.dto.ChargingPointDto;
import com.scube.chargingstation.dto.ChargingRequestRespDto;
import com.scube.chargingstation.dto.incoming.ChargingStationWiseReportIncomingDto;
import com.scube.chargingstation.entity.ChargingPointEntity;
import com.scube.chargingstation.entity.ChargingRequestEntity;
import com.scube.chargingstation.entity.PartnerInfoEntity;
import com.scube.chargingstation.exception.BRSException;
import com.scube.chargingstation.repository.ChargingPointRepository;
import com.scube.chargingstation.repository.ChargingRequestRepository;
import com.scube.chargingstation.repository.PartnerRepository;
import com.scube.chargingstation.util.DateUtils;
import com.scube.chargingstation.util.StringNullEmpty;

@Service
public class ReportServiceImpl implements ReportService {

	@Autowired
	ChargingPointRepository chargingPointRepository;
	
	@Autowired
	ChargingRequestRepository chargingRequestRepository;
	
	@Autowired
	PartnerRepository partnerRepository;
	
	
	@Override
	public List<ChargingRequestRespDto> getChargingDetailsForAllStations(
			ChargingStationWiseReportIncomingDto chargingStationWiseReportIncomingDto) {
		// TODO Auto-generated method stub
		
		System.out.println("SDate=="+chargingStationWiseReportIncomingDto.getStartDate()+"EDate==="+chargingStationWiseReportIncomingDto.getEndDate());
		
		if (chargingStationWiseReportIncomingDto.getStartDate() == null && chargingStationWiseReportIncomingDto.getEndDate() ==  null) {
			
			throw BRSException.throwException("Error : Please select your Date Range");
		}
		String endInputDate = chargingStationWiseReportIncomingDto.getEndDate();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = new GregorianCalendar();
			try {
				calendar.setTime(simpleDateFormat.parse(endInputDate));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		calendar.add(Calendar.DATE, 1);
		String convertEndInputDate = simpleDateFormat.format(calendar.getTime());
		System.out.println("End input Date :---->>> " + convertEndInputDate);
		
		List<ChargingRequestEntity> chargingRequestEntities = chargingRequestRepository.getChargingRequestEntityByDateRange(chargingStationWiseReportIncomingDto.getStartDate(), convertEndInputDate);
		
		List<ChargingRequestRespDto> respList=new ArrayList<ChargingRequestRespDto>(); //output list
		
		 for(ChargingRequestEntity entity : chargingRequestEntities) { 
			 
			ChargingRequestRespDto   CPDto=new ChargingRequestRespDto();
		 	double originalKwh = entity.getFinalKwh();
			String roundOfKwh = String.format("%.2f", originalKwh);
			double originalFinalAmt = entity.getFinalAmount();
			String roundOfFinalAmt = String.format("%.2f", originalFinalAmt);
		 
		 
			CPDto.setChargePoint(entity.getChargingPointEntity().getChargingPointId());
			CPDto.setChargePointName(entity.getChargingPointEntity().getName());
			CPDto.setChargePointAddr(entity.getChargingPointEntity().getAddress() + entity.getChargingPointEntity().getAddress2() + entity.getChargingPointEntity().getPincode());
			ChargingPointEntity chargingPointEntity=chargingPointRepository.getById(entity.getChargingPointEntity().getId());
			PartnerInfoEntity partnerInfoEntity= partnerRepository.getById(chargingPointEntity.getPartner().getId());
			CPDto.setPartnerName(partnerInfoEntity.getPartnerName());
			
			CPDto.setActualAmt(roundOfFinalAmt);
			CPDto.setChargedKwh(roundOfKwh);
			CPDto.setVehicleNo(StringNullEmpty.stringNullAndEmptyToBlank(entity.getVehicleNO()));
			CPDto.setName(entity.getChargingPointEntity().getName());
			CPDto.setMobileNo(entity.getMobileNo());
			CPDto.setStartTime(DateUtils.formattedInstantToDateTimeString(entity.getStartTime()));
			CPDto.setChargingTime(StringNullEmpty.stringNullAndEmptyToBlank(entity.getChargingTime()));
			CPDto.setStopTime(DateUtils.formattedInstantToDateTimeString(entity.getStopTime()));
			CPDto.setCustName(StringNullEmpty.stringNullAndEmptyToBlank(entity.getCustName()));
			CPDto.setMobileNo(StringNullEmpty.stringNullAndEmptyToBlank(entity.getMobileNo()));
			

			 
			respList.add(CPDto);
		 }
		 
		 
		 
		return respList;
	}

}
