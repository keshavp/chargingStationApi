package com.scube.chargingstation.service;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.validation.Valid;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.itextpdf.kernel.utils.CompareTool.CompareResult;
import com.scube.chargingstation.dto.BookingResponseDto;
import com.scube.chargingstation.dto.BookingSlotsRespDto;
import com.scube.chargingstation.dto.ChargingPointConnectorRateDto;
import com.scube.chargingstation.dto.incoming.BookingRequestIncomingDto;
import com.scube.chargingstation.dto.incoming.NotificationReqDto;
import com.scube.chargingstation.dto.incoming.UserWalletRequestDto;
import com.scube.chargingstation.dto.mapper.BookingMapper;
import com.scube.chargingstation.entity.BookingRequestEntity;
import com.scube.chargingstation.entity.ChargingPointEntity;
import com.scube.chargingstation.entity.ConnectorEntity;
import com.scube.chargingstation.entity.UserInfoEntity;
import com.scube.chargingstation.entity.UserWalletEntity;
import com.scube.chargingstation.exception.BRSException;
import com.scube.chargingstation.exception.EntityType;
import com.scube.chargingstation.exception.ExceptionType;
import com.scube.chargingstation.repository.BookingRequestRepository;
import com.scube.chargingstation.repository.UserInfoRepository;
import com.scube.chargingstation.repository.UserWalletRepository;
import com.scube.chargingstation.util.CancellationReceiptPdf;
import com.scube.chargingstation.util.DateUtils;
import com.scube.chargingstation.util.ReceiptPdfExporter;


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
	UserInfoRepository userInfoRepository;
	
	@Autowired
	UserWalletRepository userWalletRepository;
	
	@Autowired
	ChargingPointConnectorRateService chargingPointConnectorRateService;
		
	@Value("${booking.dates}") private int endBookDate;
	
	@Value("${cancel.booking.slot}") private long cancelSlot;
	
	@Value("${chargenow.button.booking}") private long chargeNow;
	
	@Autowired
	UserPaymentService	userPaymentService;
	
	@Autowired
	NotificationService	notificationService;
	
	@Autowired
	CancellationReceiptPdf	cancellationReceiptPdf;
	
	@Value("${cancellation.refund.days}")
	private int cancellationRefundDays;
	
	@Value("${cancellation.refund.minutes}")
	private int cancellationRefundMinutes;
	
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
		
		
		UserInfoEntity userInfoEntity = userInfoRepository.findByMobilenumber(bookingRequestIncomingDto.getUserContactNo());
		if(userInfoEntity==null)
		{
			throw BRSException.throwException("Error: User does not exist"); 
		}
		
		
		UserWalletEntity userWalletEntity=userWalletRepository.findByUserInfoEntity(userInfoEntity);
		if(userWalletEntity==null)
		{
			  throw BRSException.throwException("Error: Insufficient Wallet Balance");
		}
		
		
		String inputBookTime = bookingRequestIncomingDto.getRequestedBookingDate();
		
		
		logger.info("------------------- " + inputBookTime);		
		Date convertInputBookDate = null;
		try {
			
			convertInputBookDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(inputBookTime);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw BRSException.throwException("Error : Please select your Date and Slot Time");
		}
		
		logger.info("--->>>" +convertInputBookDate );
		
		Instant inputBookDateInInstant = convertInputBookDate.toInstant();
		
		logger.info("Hiiii" + inputBookDateInInstant);
		
		
		int timeVal = bookingRequestRepository.givenDateGreaterThanCurrentDate(inputBookDateInInstant);
		
		if(timeVal == 0) {
			
			throw BRSException.throwException(EntityType.BOOKING, ExceptionType.NOT_VALID , inputBookTime); 
		}
		
		
		/*
		 * UserInfoEntity userInfoEntity =
		 * userInfoService.getUserByMobilenumber(bookingRequestIncomingDto.
		 * getUserContactNo());
		 * 
		 * if(userInfoEntity == null) {
		 * 
		 * throw BRSException.throwException("Error: User does not exist");
		 * 
		 * }
		 */
		
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
		
		UserWalletRequestDto userWalletRequestDto = new UserWalletRequestDto();
		
		ChargingPointConnectorRateDto chargingPointConnectorRateDto = chargingPointConnectorRateService.getConnectorByChargingPointNameAndConnectorIdAndAmount(bookingRequestIncomingDto.getChargingPointId(), 
				bookingRequestIncomingDto.getConnectorId(), bookingRequestIncomingDto.getRequestedAmount());
		
		logger.info("-----" + "Rates are : " + chargingPointConnectorRateDto + "-----");
			
		if(chargingPointConnectorRateDto == null) {
			
			throw BRSException.throwException("Error : No Rate Present for selected Charging Point and Connector");
			
		}
		
		logger.info("-----" + "Rates are : " + chargingPointConnectorRateDto.getCancelBookingAmount() + "-----");
		
		int isSlotAvailable = bookingRequestRepository.isBookingSlotIsAvailable("SCHEDULED", bookingRequestIncomingDto.getChargingPointId(), bookingRequestIncomingDto.getConnectorId(),
				inputBookDateInInstant, endTime);
		
		if(isSlotAvailable == 1) {
			throw BRSException.throwException(EntityType.BOOKING, ExceptionType.NOT_AVAILABLE , inputBookTime); 
		}
		
		
		Double balance=0.0;
		Double dCurBal=userWalletEntity.getCurrentBalance();
		Double bookAmt=chargingPointConnectorRateDto.getCancelBookingAmount(); // book amount
			
		
		if(dCurBal<bookAmt)
		{
			  throw BRSException.throwException("Error: Insufficient Wallet Balance");
		}
		
		
		
		userWalletRequestDto.setMobileUser_Id(userInfoEntity.getMobilenumber());
		userWalletRequestDto.setTransactionType("Debit");
		userWalletRequestDto.setRequestAmount(String.valueOf(chargingPointConnectorRateDto.getCancelBookingAmount()));
		userWalletRequestDto.setPaymentFor("Debit -Booking amount");
		
		userPaymentService.processWalletMoney(userWalletRequestDto);
		
		BookingRequestEntity bookingRequestEntity = new BookingRequestEntity();
		
		bookingRequestEntity.setUserInfoEntity(userInfoEntity);
		bookingRequestEntity.setIsdeleted("N");
		bookingRequestEntity.setHourReminderStatus("N");
		bookingRequestEntity.setOneDayReminderStatus("N");
		bookingRequestEntity.setChargingPointEntity(chargingPointEntity);
		bookingRequestEntity.setBookingStatus("SCHEDULED");
		bookingRequestEntity.setConnectorEntity(connectorEntity);
		bookingRequestEntity.setRequestAmount(bookingRequestIncomingDto.getRequestedAmount());
		bookingRequestEntity.setBookingTime(inputBookDateInInstant);
		bookingRequestEntity.setCustName(bookingRequestIncomingDto.getCustName());
		bookingRequestEntity.setMobileNo(bookingRequestIncomingDto.getCustMobileNo());
		bookingRequestEntity.setVehicleNO(bookingRequestIncomingDto.getCustVehicleNo());
		bookingRequestEntity.setBookingEndtime(endTime);
		bookingRequestEntity.setBookingAmount(chargingPointConnectorRateDto.getCancelBookingAmount());
		
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
		
		String selectedConnectorId = connectorEntity.getId();
		logger.info("Selected Connector ID : " + selectedConnectorId);
		
		 
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

		bookingSlotsRespDto=slotCreation(bookingRequestIncomingDto,convertSlotInfoIntoSeconds, selectedConnectorId);
				
		return bookingSlotsRespDto;
	}

	
	public List<BookingSlotsRespDto> slotCreation(BookingRequestIncomingDto bookingRequestIncomingDto,long convertSlotInfoIntoSeconds, String selectedConnectorId) {
		// TODO Auto-generated method stub
		
		logger.info("Selected connector and charging point :" + selectedConnectorId);
		
		List<BookingSlotsRespDto> bookingSlotsRespDto = new ArrayList<BookingSlotsRespDto>();
		
		List<String> slotTimeList = new ArrayList<String>();
		
		List<String> checkSlotDateTimeList = new ArrayList<String>();

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
			
			dateObj1=simpleDateFormat.parse(startDate + " " + startTimeOfStation);
			dateObj2 = simpleDateFormat.parse(startDate + " " + endTimeOfStation);	
			
			logger.info("-----" + "Date Start : " + dateObj1 + "-----");
			
			logger.info("-----" + "Date End : " + dateObj2 + "-----");
			
			long diffBetweenStartTimeAndEndTime = dateObj1.getTime(); 
			
			String formatSlotTime = "";
			
			String formatSlotDateTime = "";
			
			SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");  // --> Converts slots in Time (HH:mm)
			
			while (diffBetweenStartTimeAndEndTime < dateObj2.getTime()) {	
				
				Date slot = new Date(diffBetweenStartTimeAndEndTime);
				logger.info("-----" + "Slot Start Date and End Date in (Date format) :" + slot + "------");
				formatSlotTime = timeFormat.format(slot);	
				diffBetweenStartTimeAndEndTime += convertSlotInfoIntoSeconds;
				
				logger.info("-----" + "Slots Time are " + formatSlotTime + "---");
				
				formatSlotDateTime = simpleDateFormat.format(slot);
								
				logger.info("-----" + "Slots Date & Time are " + formatSlotDateTime + "---");
				
//				dto.setSlotDateAndTime(formatSlotTime);	
				slotTimeList.add(formatSlotTime);
				checkSlotDateTimeList.add(formatSlotDateTime);
			}
			
			String concatDateAndTimeForSlot = "";
			String getAmPmMarker1 = "";
			
			System.out.println("Slots in Time Format are : " + slotTimeList);
			
			int count = 0;
			
			for (int i=0; i<slotTimeList.size(); i++) {	
				
				BookingSlotsRespDto slotsRespDto = new BookingSlotsRespDto();
				
				System.out.println("Slots in Array List are : " + slotTimeList.get(i) + checkSlotDateTimeList.get(i));
				
				List<BookingRequestEntity> bookingRequestEntity = bookingRequestRepository.findByBookingTimeAndBookingStatus(checkSlotDateTimeList.get(i), "SCHEDULED", selectedConnectorId);
				
				logger.info("----" + "Entity " + bookingRequestEntity.size() + "----");
				
				if(bookingRequestEntity.size()>0) {
					
						logger.info("-----" + "Slot is already Booked " + "-----");
						slotsRespDto.setSlotAvailability("BOOKED");
						slotsRespDto.setSlotId(count);
						
				}			
				else {
					
						logger.info("-----" + "Slot is open. Continue your booking " + "-----");
						slotsRespDto.setSlotAvailability("OPEN");
						slotsRespDto.setSlotId(count);
				}
				
				String[] parts = slotTimeList.get(i).split(":");
				int slotHour = Integer.valueOf(parts[0]);
				
				System.out.println("-----><<<<" + slotHour);
				
				if(slotHour>11) {
					
					getAmPmMarker1 = "PM";
					
				}
				else {
					
					getAmPmMarker1 = "AM";
					
				}
				
				concatDateAndTimeForSlot = bookingRequestIncomingDto.getRequestedBookingDate() + " " + slotTimeList.get(i) + " " +  getAmPmMarker1;
				
				boolean flag = Comparetime(slotTimeList.get(i), dateObj2);
								
				if(flag)
				{
					//put in dto
					slotsRespDto.setSlotDateAndTime(concatDateAndTimeForSlot);
					slotsRespDto.setSlotId(count);
					bookingSlotsRespDto.add(slotsRespDto);

				}
				
				count ++;
				
			}
			
		}		
		catch (Exception e) {
			// TODO Auto-generated catch block
		
			e.printStackTrace();
			
		}
		
		return bookingSlotsRespDto;
		
	}

	
	public boolean Comparetime(String hour, Date dateObj2)
	{
	//	String hour = "18:00";
		System.out.println("------" + hour);
		System.out.println("------" + dateObj2);
		
		Date currentDate = new Date();
		
		String slotCurrentDate = new SimpleDateFormat("yyyy-MM-dd").format(currentDate);
		String slotInputDate = new SimpleDateFormat("yyyy-MM-dd").format(dateObj2);
		
		System.out.println("Slot Input Date (In String)" + slotInputDate);
		System.out.println("Slot Current Date (In String)" + slotCurrentDate);
		
		Calendar now = new GregorianCalendar();
		
		int nowHour = now.get(Calendar.HOUR_OF_DAY);
		System.out.println(nowHour + " " + "HOUR_OF_DAY" + "---->>");
		
		int nowMin = now.get(Calendar.MINUTE);
		System.out.println(nowMin + " " + "MINUTE" + "---->>");
		
		String[] parts = hour.split(":");

		int horaHour = Integer.valueOf(parts[0]);
		System.out.println(horaHour + "---->>");
		int horaMinute = Integer.valueOf(parts[1]);
		System.out.println(horaMinute + "---->>" );
		
		
		if(slotCurrentDate.compareTo(slotInputDate)==0) {
		
			if(60 * nowHour + nowMin < 60 * horaHour + horaMinute) {
				System.out.println("True");
				return true;
			}
			else {
				System.out.println("False");
				return false;
			}
		}
		return true;
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
		
		int count = 0;
		
		for(int i=0;i<endBookDate;i++) {
			
			BookingSlotsRespDto bookingDateDtos = new BookingSlotsRespDto();
			
			if(i==0)
				c.add(Calendar.DATE, 0);
			else 
				c.add(Calendar.DATE, 1);
			
			logger.info("---" + Calendar.DATE);
			formatSlotBookingDate = simpleDateFormat.format(c.getTime());
			
			bookingDateDtos.setSlotBookingDate(formatSlotBookingDate);
			bookingDateDtos.setDateId(count);
			
			count++;
			
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
				responseDto.setCancelNow("Y");
				
			}
			else {
				
				logger.info("-----" + "NOT ALLOWED TO CANCEL" + "-----");
				responseDto.setCancelNow("N");
				
			}
			
			long reduceBookTime = startBookTimeDate.getTime() - chargeNow;
			long addBookTime = startBookTimeDate.getTime() + chargeNow;
			
			Date convertReducedDate = new Date(reduceBookTime);
			Date convertAddTimeDate = new Date(addBookTime);
			
			logger.info("---" + "Converted Sub Time" + convertReducedDate + "---");
			
			logger.info("---" + "Converted Add Time" + convertAddTimeDate + "---");
					
			if(currentDate.after(convertReducedDate) && currentDate.before(convertAddTimeDate))	{	
			
				logger.info("-----" + "CHARGE NOW IS ACTIVE" + "-----");
				responseDto.setChargeNow("Y");
			}
			
			else {
				
				logger.info("-----" + "CHARGE NOW IS NOT ACTIVE" + "-----");
				responseDto.setChargeNow("N");
				
			}
			
			responseDto.setBookingId(bookingRequestEntity.getId());
			responseDto.setBookingAmount(bookingRequestEntity.getBookingAmount());
			responseDto.setBookingDateAndTime(DateUtils.formattedInstantToDateTimeString(bookingRequestEntity.getBookingTime()));
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
		
		for( BookingRequestEntity bookingRequestEntity : bookingRequestScheduledEntities) {
		
			String title="Booking cancelled for EV-Dock.";
			String body=" Hi "+bookingRequestEntity.getUserInfoEntity().getUsername()+", your Ev car charging booking on ( "+ bookingRequestEntity.getBookingTime() +") at "+bookingRequestEntity.getChargingPointEntity().getName()+" with Evdock has been cancelled due to no show.";

			NotificationReqDto notificationReqDto =new NotificationReqDto();
			notificationReqDto.setMobileUser_Id(bookingRequestEntity.getUserInfoEntity().getMobilenumber());
			notificationReqDto.setTitle(title);
			notificationReqDto.setBody(body);
			notificationService.sendNotification(notificationReqDto);
		
		}
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

	@Override
	public String getUpcomingBookingCancelById(String bookingId) throws Exception {
		// TODO Auto-generated method stub
		
		BookingRequestEntity bookingRequestEntity = bookingRequestRepository.findById(bookingId).get();
		
		if(bookingRequestEntity == null) {
			throw BRSException.throwException(EntityType.BOOKING, ExceptionType.ENTITY_NOT_FOUND);
		}
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Date currentDate = new Date();
		
		logger.info("-----" + "The current date in (Date Format) is :" + currentDate + "-----");
		
		Date startBookDateAndTime = new Date();
		
		long currentDateTimeFormatInLong = currentDate.getTime();
		
		String formattedBookStartTime = "";
		
		logger.info("-----" + "Time is :" + bookingRequestEntity.getBookingTime() + "-----");
		
		Instant startBookInstant = bookingRequestEntity.getBookingTime();
		
		startBookDateAndTime = Date.from(startBookInstant);
			
		formattedBookStartTime = simpleDateFormat.format(startBookDateAndTime);
		
		logger.info("-----" + "The formatted Booking Start Time in (String Format) is :" + formattedBookStartTime + "------");
		
		try {
			
			startBookDateAndTime = simpleDateFormat.parse(formattedBookStartTime);
			logger.info("-----" + "The formatted Booking Start Time is (Date Format):" + startBookDateAndTime + "------");
		
			
		} 
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(currentDateTimeFormatInLong<(startBookDateAndTime.getTime()-cancelSlot)) {
			
			logger.info("-----" + "CANCEL" + "-----");
			
		}
		
		else {
				throw BRSException.throwException("Error : You cannot cancel this slot now");
			
		}
		
		if(bookingRequestEntity.getBookingStatus().equals("CANCELLED")) {
			 throw BRSException.throwException(EntityType.BOOKING, ExceptionType.ALREADY_EXIST_ENTITY); 
		}
			
			int bookingTimeDiff = bookingRequestRepository.getTimeInMinuteDiff(bookingId);

		
			if(bookingTimeDiff > cancellationRefundMinutes) {
			
					bookingRequestEntity.setBookingStatus("CANCELLED");
					bookingRequestRepository.save(bookingRequestEntity);
				
					UserWalletRequestDto	userWalletRequestDto = new UserWalletRequestDto();
					
					userWalletRequestDto.setTransactionType("Credit");
					userWalletRequestDto.setMobileUser_Id(bookingRequestEntity.getUserInfoEntity().getMobilenumber());
					userWalletRequestDto.setRequestAmount(String.valueOf(bookingRequestEntity.getBookingAmount()));
					userWalletRequestDto.setPaymentFor("Refund- Booking cancelled");
					
					userPaymentService.processWalletMoney(userWalletRequestDto);
				
				return "Booking successful cancellation. Refund Amount "+userWalletRequestDto.getRequestAmount()+" is added in your wallet.";
			}
		
			BookingRequestEntity bookingRequestPdfEntity =   cancellationReceiptPdf.generatePdf(bookingRequestEntity);
			
			bookingRequestEntity.setBookingStatus("CANCELLED");
			bookingRequestEntity.setInvoiceFilePath(bookingRequestPdfEntity.getInvoiceFilePath());
			bookingRequestEntity.setReceiptNo(bookingRequestPdfEntity.getReceiptNo());
			bookingRequestRepository.save(bookingRequestEntity);
			
		return "Booking successful cancellation. no refund";
	}

	@Override
	public void oneDayBeforeBookingReminderSchedulers() {
		// TODO Auto-generated method stub
		
		List<BookingRequestEntity>  bookingRequestEntities = bookingRequestRepository.oneDayBookingReminderSchedulersByScheduled(1);
		
		for(BookingRequestEntity  bookingRequestEntity : bookingRequestEntities) {
			
			String title="Booking reminder for EV-Dock.";
			String body=" Hi "+bookingRequestEntity.getUserInfoEntity().getUsername()+", you have an EV car charging booking on ( "+ DateUtils.formattedInstantToSimpleDateTimeFormat(bookingRequestEntity.getBookingTime()) +") at "+bookingRequestEntity.getChargingPointEntity().getName()+" with Evdock .";
			
			NotificationReqDto notificationReqDto =new NotificationReqDto();
			notificationReqDto.setMobileUser_Id(bookingRequestEntity.getUserInfoEntity().getMobilenumber());
			notificationReqDto.setTitle(title);
			notificationReqDto.setBody(body);
			notificationService.sendNotification(notificationReqDto);
			
		}
	}

	@Override
	public void oneHourBeforeBookingReminderSchedulers() {
		// TODO Auto-generated method stub
		
		List<BookingRequestEntity>  bookingRequestEntities = bookingRequestRepository.oneHourBeforeBookingReminderSchedulersByScheduled(1);
		
		for(BookingRequestEntity  bookingRequestEntity : bookingRequestEntities) {
			
			String title="Booking reminder for EV-Dock.";
			String body=" Hi "+bookingRequestEntity.getUserInfoEntity().getUsername()+", you have an EV car charging booking on ( "+ DateUtils.formattedInstantToSimpleDateTimeFormat(bookingRequestEntity.getBookingTime()) +") at "+bookingRequestEntity.getChargingPointEntity().getName()+" with Evdock .";
			
			NotificationReqDto notificationReqDto =new NotificationReqDto();
			notificationReqDto.setMobileUser_Id(bookingRequestEntity.getUserInfoEntity().getMobilenumber());
			notificationReqDto.setTitle(title);
			notificationReqDto.setBody(body);
			notificationService.sendNotification(notificationReqDto);
			
		}
	}
	
	@Override
	public boolean bookingReminderById(String Id) {
		// TODO Auto-generated method stub
		
		BookingRequestEntity  bookingRequestEntity = bookingRequestRepository.findById(Id).get();
			
			String title="Booking reminder for EV-Dock.";	
			String body=" Hi "+bookingRequestEntity.getUserInfoEntity().getUsername()+", you have an EV car charging booking on ( "+ DateUtils.formattedInstantToSimpleDateTimeFormat(bookingRequestEntity.getBookingTime()) +") at "+bookingRequestEntity.getChargingPointEntity().getName()+" with Evdock .";
			
			NotificationReqDto notificationReqDto =new NotificationReqDto();
			notificationReqDto.setMobileUser_Id(bookingRequestEntity.getUserInfoEntity().getMobilenumber());
			notificationReqDto.setTitle(title);
			notificationReqDto.setBody(body);
			notificationService.sendNotification(notificationReqDto);
			
			return true;
	}
}
