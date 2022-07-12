package com.scube.chargingstation.service;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.scube.chargingstation.dto.BookingSlotsRespDto;
import com.scube.chargingstation.dto.incoming.BookingRequestIncomingDto;
import com.scube.chargingstation.entity.BookingRequestEntity;
import com.scube.chargingstation.entity.ChargerTypeEntity;
import com.scube.chargingstation.entity.ChargingPointEntity;
import com.scube.chargingstation.entity.ConnectorEntity;
import com.scube.chargingstation.entity.UserInfoEntity;
import com.scube.chargingstation.exception.BRSException;
import com.scube.chargingstation.repository.BookingRequestRepository;
import com.scube.chargingstation.util.DateUtils;
import com.scube.chargingstation.repository.ConnectorRepository;


@Service
public class BookingRequestServiceImpl implements BookingRequestService{

	private static final Logger logger = LoggerFactory.getLogger(BookingRequestServiceImpl.class);
	
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	ChargingPointService chargingPointService;
	
	@Autowired
	ConnectorService connectorService;
	
	@Autowired
	BookingRequestRepository bookingRequestRepository;
	
	@Autowired
	ConnectorRepository connectorRepository;
		
	@Value("${booking.dates.cronTime}") private int endBookDate;
	
	@Override
	public boolean bookNewChargeSlot(@Valid BookingRequestIncomingDto bookingRequestIncomingDto) {
		// TODO Auto-generated method stub
		logger.info("***BookingRequestServiceImpl bookNewChargeSlot***");
		
		if(bookingRequestIncomingDto.getChargingPointName() == null || bookingRequestIncomingDto.getChargingPointName().trim().isEmpty()) {
			
			throw BRSException.throwException("Error : Charging Point Name cannot be empty");
			
		}
		
		
		if(bookingRequestIncomingDto.getConnectorId() == null || bookingRequestIncomingDto.getConnectorId().trim().isEmpty()) {
			
			throw BRSException.throwException("Error : Connector Name cannot be empty");
			
		}

		
		if(bookingRequestIncomingDto.getUserContactNo() == null || bookingRequestIncomingDto.getUserContactNo().trim().isEmpty()) {
			
			throw BRSException.throwException("Error : Contact No cannot be empty");
			
		}
		
		UserInfoEntity userInfoEntity = userInfoService.getUserByMobilenumber(bookingRequestIncomingDto.getUserContactNo());
		
		ChargingPointEntity chargingPointEntity = chargingPointService.getChargingPointEntityByChargingPointName(bookingRequestIncomingDto.getChargingPointName());
		
	//	ConnectorEntity connectorEntity = connectorService.getConnectorEntityByConnectorId(bookingRequestIncomingDto.getConnectorId());
		
		ConnectorEntity	connectorEntity = connectorService.getConnectorEntityByConnectorIdAndChargingPointEntity(bookingRequestIncomingDto.getConnectorId(),chargingPointEntity) ;
		if(connectorEntity==null)
		{
			throw BRSException.throwException("Error: Connector does not exist"); 
		}
		
		BookingRequestEntity bookingRequestEntity = new BookingRequestEntity();
		
		bookingRequestEntity.setUserInfoEntity(userInfoEntity);
		bookingRequestEntity.setIsdeleted("N");
		bookingRequestEntity.setChargingPointEntity(chargingPointEntity);
		bookingRequestEntity.setBookingStatus("SCHEDULED");
		bookingRequestEntity.setChargerTypeEntity(connectorEntity);
		bookingRequestEntity.setBookingTime(null);
		
		bookingRequestRepository.save(bookingRequestEntity);
		
		return true;
	}

	@Override
	public List<BookingSlotsRespDto> getAvailableChargingSlotsForChargingPointAndConnector(BookingRequestIncomingDto bookingRequestIncomingDto) {
		// TODO Auto-generated method stub
		
		if(bookingRequestIncomingDto.getChargingPointName()==null || bookingRequestIncomingDto.getChargingPointName().trim().isEmpty()) {
			
			throw BRSException.throwException("Error : Charge Point Name cannot be blank");
			
		}
		
		ChargingPointEntity chargingPointEntity = chargingPointService.getChargingPointEntityByChargingPointName(bookingRequestIncomingDto.getChargingPointName());
		
		logger.info("-----" + "Charging Point Info" + chargingPointEntity + "------");
		
		if(chargingPointEntity == null) {
			
			throw BRSException.throwException("Error: No Charging Point present");
			
		}
		
		if(bookingRequestIncomingDto.getConnectorId()==null || bookingRequestIncomingDto.getConnectorId().trim().isEmpty()) {
			
			throw BRSException.throwException("Error : Connector Name cannot be blank");
			
		}
		
		ConnectorEntity	connectorEntity = connectorService.getConnectorEntityByConnectorIdAndChargingPointEntity(bookingRequestIncomingDto.getConnectorId() ,chargingPointEntity) ;
		
		logger.info("-----" + "The Connector Entities are : " + connectorEntity + "------");
		
		if(connectorEntity == null) {
			
			throw BRSException.throwException("Error: No Connector present");
			
		}
		
		 
		if(bookingRequestIncomingDto.getRequestedBookingDate()==null || bookingRequestIncomingDto.getRequestedBookingDate().trim().isEmpty()) {
			
			throw BRSException.throwException("Error : Requested Booking Date cannot be blank");
			
		}
		
		
		if(DateUtils.isValidDate(bookingRequestIncomingDto.getRequestedBookingDate()) == false) {
			
			throw BRSException.throwException("Error : Requested Booking Date Format is Incorrect");
			
		}
		
		
		String slotInfo = connectorEntity.getSlotInterval();
		
		logger.info("-----" + "Connector Info is" + slotInfo + "-----");
		
		if(slotInfo == null) {
			
			throw BRSException.throwException("Error : No Slot Interval present");
			
		}
		
		long convertedSlotInfo = Long.parseLong(slotInfo);
		
		long convertSlotInfoIntoSeconds = convertedSlotInfo*60000;
		
		String reqBookDate = bookingRequestIncomingDto.getRequestedBookingDate();
		
		logger.info("-----" + "The Requested Booking Date is : " + reqBookDate + "-----");
		
		List<BookingSlotsRespDto> bookingSlotsRespDto = new ArrayList<BookingSlotsRespDto>();

		bookingSlotsRespDto=slotCreation(bookingRequestIncomingDto,convertSlotInfoIntoSeconds);
				
		return bookingSlotsRespDto;
	}

	
	public List<BookingSlotsRespDto> slotCreation(BookingRequestIncomingDto bookingRequestIncomingDto,long convertSlotInfoIntoSeconds) {
		// TODO Auto-generated method stub
		
		List<BookingSlotsRespDto> bookingSlotsRespDto = new ArrayList<BookingSlotsRespDto>();

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss aa");
		
		String startDate = bookingRequestIncomingDto.getRequestedBookingDate();
		
		logger.info("-----" + "The Start Input Date is : " + startDate + "-----");  
		
		ChargingPointEntity chargingPointEntity = chargingPointService.getChargingPointEntityById(bookingRequestIncomingDto.getChargingPointId());
		
		Date currentTimeAndDate = new Date();
		
		String startTime = chargingPointEntity.getStartTime();  // --> Start Time
		
		logger.info("-----" + "The station start time is : " + startTime + "-----");
		
		String endTime = chargingPointEntity.getEndTime();  // --> End Time
		
		logger.info("-----" + "The station end time is : " + startTime + "-----");
	
		String currentDateInString = simpleDateFormat.format(currentTimeAndDate);
		
		logger.info("-----" + "The current Date is : " + currentDateInString + "-----");
		
		Date simpleCurrentDate = new Date();
		Date simpleInputDate = new Date();
		
		Date dateObj1 = new Date();
		Date dateObj2 = new Date();
		
		try {
			
			simpleCurrentDate = new SimpleDateFormat("yyyy-MM-dd").parse(currentDateInString);
			
			logger.info("-----" + "The Current Date Format is : " + simpleCurrentDate + "-----" + "Hello");
			
			simpleInputDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
			
			logger.info("-----" + "The Input Date Format is : " + simpleInputDate + "-----");
					
			//comapre i/p date & current dt
			// if i/p date == currenet date
			
			if(simpleCurrentDate.compareTo(simpleInputDate)==0)// Check if the input date is todays date
			{
				Calendar currentTimeDate = new GregorianCalendar();
				
				String roundedDateTime = currentTimeDate.get(Calendar.HOUR_OF_DAY) + 1 + ":" + "00" + ":" + "00";
				
				logger.info("-----" + "The round of Date and Time is : " + roundedDateTime);

				String dtforslot =startDate + " " + roundedDateTime;
				
				logger.info("-----" + "Tdtforslot : " + dtforslot);
				
				dateObj1 = simpleDateFormat.parse(dtforslot);
				dateObj2 = simpleDateFormat.parse(startDate + " " + endTime);
				
			}
			else { //future date
				
				dateObj1=simpleDateFormat.parse(startDate + " " + startTime);
				dateObj2 = simpleDateFormat.parse(startDate + " " + endTime);
		
			}
			
			logger.info("-----" + "Date Start : " + dateObj1 + "-----");
			
			logger.info("-----" + "Date End : " + dateObj2 + "-----");
			
			long diffBetweenStartTimeAndEndTime = dateObj1.getTime(); 
			
			String formatSlotDateAndTime = "";
			
			while (diffBetweenStartTimeAndEndTime<dateObj2.getTime()) {			
				
				BookingSlotsRespDto dto=new BookingSlotsRespDto();
				
				Date slot = new Date(diffBetweenStartTimeAndEndTime);
				formatSlotDateAndTime = simpleDateFormat.format(slot);	
				diffBetweenStartTimeAndEndTime += convertSlotInfoIntoSeconds;
				
				logger.info("-----" + "Slots are " + formatSlotDateAndTime + "---");
				
				List<BookingRequestEntity> bookingRequestEntity = bookingRequestRepository.findByBookingTimeAndBookingStatus(formatSlotDateAndTime, "SCHEDULED");
				
				logger.info("----" + "Entity " + bookingRequestEntity + "----");
				
				if(bookingRequestEntity.size()>0) {
					
						logger.info("-----" + "Slot is already Booked " + "-----");
						dto.setSlotAvailability("BOOKED");
				
				}			
				else {
					
						logger.info("-----" + "Slot is open. Continue your booking " + "-----");
						dto.setSlotAvailability("OPEN");
					
				}
				
				dto.setSlotDateAndTime(formatSlotDateAndTime);
				bookingSlotsRespDto.add(dto);
								
			}
			
		}		
		catch (Exception e) {
			// TODO Auto-generated catch block
		
			e.printStackTrace();
			
		}
		
		return bookingSlotsRespDto;
		
	}

	@Override
	public List<BookingSlotsRespDto> getBookingDatesForChargingPointAndConnector(@Valid BookingRequestIncomingDto bookingRequestIncomingDto) {
		// TODO Auto-generated method stub
		
		List<BookingSlotsRespDto> bookingSlotsRespDto = new ArrayList<BookingSlotsRespDto>();
		
		Calendar calendar = GregorianCalendar.getInstance();
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		Date currentStartDate = new Date();
		
		String currentDateOfWeek = simpleDateFormat.format(currentStartDate);
		
		try {
			
			currentStartDate = new SimpleDateFormat("yyyy-MM-dd").parse(currentDateOfWeek);
			logger.info("-----" + "The Current Start Date in date format is : " + currentStartDate + "-----");
			
		} 
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		logger.info("-----" + "The current start date in string format is : " + currentDateOfWeek + "-----");
		
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		calendar.add(Calendar.DATE, 6);
		
		String currentEndDateOfWeek = simpleDateFormat.format(calendar.getTime());
		
		logger.info("-----" + "The current end date in string format is : " + currentEndDateOfWeek + "-----");
			
		Date lastDateOfWeek = new Date();
		
		try {
			
			lastDateOfWeek = simpleDateFormat.parse(currentEndDateOfWeek);
			logger.info("-----" + "The Current End Date in date format is : " + lastDateOfWeek + "-----");
			
		} 
		catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String formatSlotBookingDate = "";
		
		Calendar c = Calendar.getInstance();
		
		try {
			
			c.setTime(simpleDateFormat.parse(currentDateOfWeek));
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			
		logger.info("-----" + "The End Date Slot is : " + endBookDate + "-----");
		
		for(int i=0;i<endBookDate;i++) {
			
			BookingSlotsRespDto bookingDateDtos = new BookingSlotsRespDto();
			
			if(i==0)
				c.add(Calendar.DATE, 0);
			else 
				c.add(Calendar.DATE, 1);
			
			logger.info("---" + Calendar.DATE);
			formatSlotBookingDate = simpleDateFormat.format(c.getTime());
			
			bookingDateDtos.setSlotBookingDate(formatSlotBookingDate);
			bookingSlotsRespDto.add(bookingDateDtos);
		}
		
		return bookingSlotsRespDto;
	}
	 
}
