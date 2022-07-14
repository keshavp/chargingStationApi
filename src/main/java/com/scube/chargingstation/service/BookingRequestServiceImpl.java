package com.scube.chargingstation.service;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
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

import com.scube.chargingstation.dto.BookingResponseDto;
import com.scube.chargingstation.dto.BookingSlotsRespDto;
import com.scube.chargingstation.dto.incoming.BookingRequestIncomingDto;
import com.scube.chargingstation.dto.mapper.BookingMapper;
import com.scube.chargingstation.entity.BookingRequestEntity;
import com.scube.chargingstation.entity.ChargingPointEntity;
import com.scube.chargingstation.entity.ConnectorEntity;
import com.scube.chargingstation.entity.UserInfoEntity;
import com.scube.chargingstation.exception.BRSException;
import com.scube.chargingstation.repository.BookingRequestRepository;
import com.scube.chargingstation.util.DateUtils;


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
		
	@Value("${booking.dates}") private int endBookDate;
	
	@Value("${cancel.booking.slot}") private long cancelSlot;
	
	@Value("${chargenow.button.booking}") private long chargeNow;
	
	@Override
	public boolean bookNewChargeSlot(@Valid BookingRequestIncomingDto bookingRequestIncomingDto) {
		// TODO Auto-generated method stub
		logger.info("***BookingRequestServiceImpl bookNewChargeSlot***");
		
		if(bookingRequestIncomingDto.getChargingPointId() == null || bookingRequestIncomingDto.getChargingPointId().trim().isEmpty()) {
			
			throw BRSException.throwException("Error : Charging Point ID cannot be empty");
			
		}
		
		
		if(bookingRequestIncomingDto.getConnectorId() == null || bookingRequestIncomingDto.getConnectorId().trim().isEmpty()) {
			
			throw BRSException.throwException("Error : Connector Name cannot be empty");
			
		}

		
		if(bookingRequestIncomingDto.getUserContactNo() == null || bookingRequestIncomingDto.getUserContactNo().trim().isEmpty()) {
			
			throw BRSException.throwException("Error : Contact No cannot be empty");
			
		}
		
		
		if(bookingRequestIncomingDto.getCustName() ==  null || bookingRequestIncomingDto.getCustName().trim().isEmpty()) {
			
			throw BRSException.throwException("Error : Customer Name cannot be empty");
			
		}
		
		
		if(bookingRequestIncomingDto.getCustMobileNo() ==  null || bookingRequestIncomingDto.getCustMobileNo().trim().isEmpty()) {
			
			throw BRSException.throwException("Error : Customer Mobile No cannot be empty");
			
		}
		
		
		if(bookingRequestIncomingDto.getCustVehicleNo() ==  null || bookingRequestIncomingDto.getCustVehicleNo().trim().isEmpty()) {
			
			throw BRSException.throwException("Error : Customer Vehicle No cannot be empty");
			
		}
		
		
		if(bookingRequestIncomingDto.getRequestedBookingDate() ==  null || bookingRequestIncomingDto.getRequestedBookingDate().trim().isEmpty()) {
			
			throw BRSException.throwException("Error : Booking Date cannot be empty");
			
		}
		
		
		String inputBookTime = bookingRequestIncomingDto.getRequestedBookingDate();
		
		Date convertInputBookDate = new Date();
		try {
			
			convertInputBookDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(inputBookTime);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Instant inputBookDateInInstant = convertInputBookDate.toInstant();
		
		UserInfoEntity userInfoEntity = userInfoService.getUserByMobilenumber(bookingRequestIncomingDto.getUserContactNo());
		
		if(userInfoEntity ==  null) {
			
			throw BRSException.throwException("Error: User does not exist"); 
			
		}
		
		ChargingPointEntity chargingPointEntity = chargingPointService.getChargingPointEntityByChargePointId(bookingRequestIncomingDto.getChargingPointId());
		
		if(chargingPointEntity ==  null) {
			
			throw BRSException.throwException("Error: Charging Point does not exist"); 
			
		}
		
		ConnectorEntity	connectorEntity = connectorService.getConnectorEntityByConnectorIdAndChargingPointEntity(bookingRequestIncomingDto.getConnectorId(),chargingPointEntity) ;
		
		if(connectorEntity == null) {
			
			throw BRSException.throwException("Error: Connector does not exist"); 
			
		}
		
		String slotIntervalVal = connectorEntity.getSlotInterval();
		
		logger.info("-----" + "Slot Interval is : " + slotIntervalVal + "-----");
		
		long convertedSlotInfo = Long.parseLong(slotIntervalVal);
		
		long convertSlotInfoIntoSeconds = convertedSlotInfo*60000;
		
		logger.info("-----" + "Slot Time in Long is : " + convertSlotInfoIntoSeconds + "-----");

		Instant endTime = inputBookDateInInstant.plusMillis(convertSlotInfoIntoSeconds);
		
		logger.info("-----" + "Slot End Time in Instant is : " + endTime + "-----");
		
		
		
		BookingRequestEntity bookingRequestEntity = new BookingRequestEntity();
		
		bookingRequestEntity.setUserInfoEntity(userInfoEntity);
		bookingRequestEntity.setIsdeleted("N");
		bookingRequestEntity.setChargingPointEntity(chargingPointEntity);
		bookingRequestEntity.setBookingStatus("SCHEDULED");
		bookingRequestEntity.setConnectorEntity(connectorEntity);
		bookingRequestEntity.setRequestAmount(bookingRequestIncomingDto.getRequestedAmount());
		bookingRequestEntity.setBookingTime(inputBookDateInInstant);
		bookingRequestEntity.setCustName(bookingRequestIncomingDto.getCustName());
		bookingRequestEntity.setMobileNo(bookingRequestIncomingDto.getCustMobileNo());
		bookingRequestEntity.setVehicleNO(bookingRequestIncomingDto.getCustVehicleNo());
		bookingRequestEntity.setBookingEndtime(endTime);
		
		bookingRequestRepository.save(bookingRequestEntity);
		
		return true;
	}

	@Override
	public List<BookingSlotsRespDto> getAvailableChargingSlotsForChargingPointAndConnector(BookingRequestIncomingDto bookingRequestIncomingDto) {
		// TODO Auto-generated method stub
		
		if(bookingRequestIncomingDto.getChargingPointId()==null || bookingRequestIncomingDto.getChargingPointId().trim().isEmpty()) {
			
			throw BRSException.throwException("Error : Charge Point ID cannot be blank");
			
		}
		
		ChargingPointEntity chargingPointEntity = chargingPointService.getChargingPointEntityByChargePointId(bookingRequestIncomingDto.getChargingPointId());
		
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

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		String startDate = bookingRequestIncomingDto.getRequestedBookingDate();
		
		logger.info("-----" + "The Start Input Date is : " + startDate + "-----");  
		
		ChargingPointEntity chargingPointEntity = chargingPointService.getChargingPointEntityByChargePointId(bookingRequestIncomingDto.getChargingPointId());
		
		Date currentTimeAndDate = new Date();
		
		logger.info("-----" + "The current Date and Time is : " + currentTimeAndDate + "-----");
		
		String startTime = chargingPointEntity.getStartTime();  // --> Start Time
		
		logger.info("-----" + "The station start time is : " + startTime + "-----");
		
		Date convertStartTime = new Date();
		
		String getAmPmOfStartTime = "";
		
		String startTimeOfStation = "";
		
		try {
			
			convertStartTime = new SimpleDateFormat("HH:mm").parse(startTime);
			
			logger.info("-----" + "The Start Time (Date Format) is : " + convertStartTime);
				
			getAmPmOfStartTime = convertStartTime.getHours()>=12 ? "PM" : "AM";
				logger.info("-----" + "Time marker is :" + getAmPmOfStartTime + "------");
				startTimeOfStation = chargingPointEntity.getStartTime() + ":" + "00" + " " + getAmPmOfStartTime;				
				logger.info("-----" + "Converted Start Time of Station :" + startTimeOfStation + "------");
					
		} 
		catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String endTime = chargingPointEntity.getEndTime();  // --> End Time
		
		logger.info("-----" + "The station end time is : " + endTime + "-----");
		
		Date convertEndTime = new Date();
		
		String getAmPmOfEndTime = "";
		
		String endTimeOfStation = "";
		
		try {
	
			convertEndTime = new SimpleDateFormat("HH:mm").parse(endTime);
			
			logger.info("-----" + "The End Time (Date Format) is : " + convertEndTime);
				
				getAmPmOfEndTime = convertStartTime.getHours()>=12 ? "AM" : "PM";
				logger.info("-----" + "Time marker is :" + getAmPmOfEndTime + "------");
				endTimeOfStation = chargingPointEntity.getEndTime() + ":" + "00" + " " + getAmPmOfEndTime;				
				logger.info("-----" + "Converted End Time of Station :" + endTimeOfStation + "------");
					
		} 
		catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
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
				dateObj2 = simpleDateFormat.parse(startDate + " " + endTimeOfStation);
				
			}
			else { //future date
				
				dateObj1=simpleDateFormat.parse(startDate + " " + startTimeOfStation);
				dateObj2 = simpleDateFormat.parse(startDate + " " + endTimeOfStation);
		
			}
			
			logger.info("-----" + "Date Start : " + dateObj1 + "-----");
			
			logger.info("-----" + "Date End : " + dateObj2 + "-----");
			
			long diffBetweenStartTimeAndEndTime = dateObj1.getTime(); 
			
			String formatSlotDateAndTime = "";
			
			while (diffBetweenStartTimeAndEndTime<dateObj2.getTime()) {			
				
				BookingSlotsRespDto dto=new BookingSlotsRespDto();
				
				Date slot = new Date(diffBetweenStartTimeAndEndTime);
				logger.info("-----" + "Slot Start Date and End Date in (Date format) :" + slot + "------");
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
	public List<BookingSlotsRespDto> getBookingDatesForChargingPointAndConnector() {
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

	@Override
	public List<BookingResponseDto> getBookingHistoryForUserByUserId(String userMobileNo) {
		// TODO Auto-generated method stub
		
		logger.info("***BookingRequestServiceImpl getBookingHistoryForUserByUserId***");
		
		UserInfoEntity userInfoEntity = userInfoService.getUserByMobilenumber(userMobileNo);
		
		List<BookingRequestEntity> bookingRequestEntities = bookingRequestRepository.findByUserInfoEntity(userInfoEntity);
		
		return BookingMapper.toBookingResponseDtos(bookingRequestEntities);
	}

	@Override
	public List<BookingResponseDto> getUpcomingBookingDetailsForUserByUserId(String userMobileNo) {
		// TODO Auto-generated method stub
		
		logger.info("***BookingRequestServiceImpl getUpcomingBookingDetailsForUserByUserId***");
			
		List<BookingResponseDto> bookingResponseDtos = new ArrayList<BookingResponseDto>();
		
		UserInfoEntity userInfoEntity = userInfoService.getUserByMobilenumber(userMobileNo);
		
		List<BookingRequestEntity> bookingRequestEntities = bookingRequestRepository.getUpcomingBookingTimeByUserInfoEntity(userInfoEntity);
		
		if(bookingRequestEntities == null) {
			
			throw BRSException.throwException("Error : No Booking Request Present");
			
		}
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Date currentDate = new Date();
		
		logger.info("-----" + "The current date in (Date Format) is :" + currentDate + "-----");
		
		Date startBookTimeDate = new Date();
		
		long currentDateFormatInLong = currentDate.getTime();
		
		String formattedBookStartTime = "";
		
		for(BookingRequestEntity bookingRequestEntity : bookingRequestEntities) {
			
			logger.info("-----" + "Time is :" + bookingRequestEntity.getBookingTime() + "-----");
			
			Instant startBookInstant = bookingRequestEntity.getBookingTime();
			
			startBookTimeDate = Date.from(startBookInstant);
				
			formattedBookStartTime = simpleDateFormat.format(startBookTimeDate);
			
			logger.info("-----" + "The formatted Booking Start Time is :" + formattedBookStartTime + "------");
			
			try {
				
				startBookTimeDate = simpleDateFormat.parse(formattedBookStartTime);
				logger.info("-----" + "The formatted Booking Start Time is (Date Format):" + startBookTimeDate + "------");
			
				
			} 
			catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			BookingResponseDto responseDto = new BookingResponseDto();
			
			if(currentDateFormatInLong<startBookTimeDate.getTime()-cancelSlot) {
		
				logger.info("-----" + "CANCEL SLOT" + "-----");
				responseDto.setCancelNow("CANCEL NOW");
				
			}
			else {
				
				logger.info("-----" + "NOT ALLOWED TO CANCEL" + "-----");
				responseDto.setCancelNow("NOT ALLOWED");
				
			}
			
			long reduceBookTime = startBookTimeDate.getTime() - chargeNow;
			long addBookTime = startBookTimeDate.getTime() + chargeNow;
			
			Date convertReducedDate = new Date(reduceBookTime);
			Date convertAddTimeDate = new Date(addBookTime);
			
			logger.info("---" + "Converted Sub Time" + convertReducedDate + "---");
			
			logger.info("---" + "Converted Add Time" + convertAddTimeDate + "---");
					
			if(currentDate.after(convertReducedDate) && currentDate.before(convertAddTimeDate))	{	
			
				logger.info("---" + "Current Time Now" + currentDateFormatInLong + "---");
				responseDto.setChargeNow("CHARGE NOW");
			}
			
			responseDto.setBookingAmount(bookingRequestEntity.getBookingAmount());
			responseDto.setBookingDateAndTime(DateUtils.formattedInstantToDateTimeString(bookingRequestEntity.getBookingTime()));
			responseDto.setBookingId(bookingRequestEntity.getId());
			responseDto.setBookingStatus(bookingRequestEntity.getBookingStatus());
			responseDto.setCustName(bookingRequestEntity.getCustName());
			responseDto.setCustMobileNo(bookingRequestEntity.getMobileNo());
			responseDto.setCustVehicleNo(bookingRequestEntity.getVehicleNO());
			responseDto.setStationId(bookingRequestEntity.getChargingPointEntity().getChargingPointId());
			responseDto.setStationName(bookingRequestEntity.getChargingPointEntity().getName());
			responseDto.setConnectorId(bookingRequestEntity.getConnectorEntity().getConnectorId());
			responseDto.setConnectorName(bookingRequestEntity.getConnectorEntity().getChargerTypeEntity().getName());
			responseDto.setRequestedAmount(bookingRequestEntity.getRequestAmount());
			
			bookingResponseDtos.add(responseDto);
			
		}
		
		return bookingResponseDtos;
	}

	@Override
	public List<BookingResponseDto> getAllUserPreviousBookingHistoryDetails() {
		// TODO Auto-generated method stub
		
		logger.info("***BookingRequestServiceImpl getAllUserPreviousBookingHistoryDetails***");
		
		List<BookingRequestEntity> bookingRequestEntity = bookingRequestRepository.getAllPreviousBookingDetailsFromBookingRequestEntities();
		
		return BookingMapper.toBookingResponseDtos(bookingRequestEntity);
	}

	@Override
	public List<BookingResponseDto> getAllUpcomingBookingDetailsInfo() {
		// TODO Auto-generated method stub
		
		logger.info("***BookingRequestServiceImpl getAllUpcomingBookingDetailsInfo***");
		
		List<BookingRequestEntity> bookingRequestEntity = bookingRequestRepository.getAllUpcomingBookingDetailsFromBookingRequestEntities();
		
		return BookingMapper.toBookingResponseDtos(bookingRequestEntity);
	}
	 
	@Override
	public void bookingAutoCancellationSchedulers() {
		// TODO Auto-generated method stub
		
		List<BookingRequestEntity> bookingRequestScheduledEntities = bookingRequestRepository.getBookingRequestAfterPassBufferTime();
		
		List<BookingRequestEntity> bookingRequestEntities = new ArrayList<BookingRequestEntity>();
		
		for( BookingRequestEntity bookingRequestEntity : bookingRequestScheduledEntities) {
			
			bookingRequestEntity.setBookingStatus("CANCELLED");
			
			bookingRequestEntities.add(bookingRequestEntity);
		}
		
		bookingRequestRepository.saveAll(bookingRequestEntities);
	}

	@Override
	public void updateBookingRequestEntityCompletedByChargingRequest(String chargingRequestId) {
		// TODO Auto-generated method stub
		
		BookingRequestEntity bookingRequestEntity = bookingRequestRepository.getBookingRequestByChargingRequest(chargingRequestId);
		
		if(bookingRequestEntity != null) {
			
			bookingRequestEntity.setBookingStatus("COMPLETED");
			bookingRequestRepository.save(bookingRequestEntity);
		}
	}
}
