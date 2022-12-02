package com.scube.chargingstation.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.scube.chargingstation.entity.BookingRequestEntity;
import com.scube.chargingstation.entity.UserInfoEntity;

@Repository
public interface BookingRequestRepository extends JpaRepository<BookingRequestEntity, String>{
	
	@Query (value = "SELECT * from booking_request where DATE_FORMAT(booking_time, \"%Y-%m-%d %H:%i:%s\") = (?1) and booking_status = (?2) and fk_connector = (?3);", nativeQuery = true)
	List<BookingRequestEntity> findByBookingTimeAndBookingStatus(String bookingTime, String bookingStatus, String connectorId);

	List<BookingRequestEntity> findByBookingTime(String bookingTime);
	
	List<BookingRequestEntity> findByBookingStatus( String bookingStatus);
	
	@Query (value = "SELECT * from booking_request where fk_user=(?1) and booking_time <= NOW() and booking_status NOT IN ('SCHEDULED') order by booking_time desc;", nativeQuery = true)
	List<BookingRequestEntity> getBookingHistoryByUserInfoEntity(UserInfoEntity id);
	
	@Query (value = "SELECT * from booking_request where fk_user=(?1) and booking_time >= CURDATE() and booking_status = 'SCHEDULED' order by booking_time asc;", nativeQuery = true)
	List<BookingRequestEntity> getUpcomingBookingScheduleByUserInfoEntity(UserInfoEntity id);
	
	
	@Query(value = "SELECT * from booking_request where DATE_FORMAT(booking_time, \"%Y-%m-%d %H:%i:%s\") <= CURDATE() order by booking_time desc;", nativeQuery = true)
	List<BookingRequestEntity> getAllPreviousBookingDetailsFromBookingRequestEntities();
	
	@Query(value = "SELECT * from booking_request where DATE_FORMAT(booking_time, \"%Y-%m-%d %H:%i:%s\") >= CURDATE() order by booking_time asc;", nativeQuery = true)
	List<BookingRequestEntity> getAllUpcomingBookingDetailsFromBookingRequestEntities();

	@Query (value = "SELECT * FROM booking_request  where booking_status = 'Starting' and booking_endtime between date_add(NOW(),interval 3 minute) and date_add(NOW(),interval 5 minute)" , nativeQuery = true)
	List<BookingRequestEntity> findBookingRequestsToStop();
	
	@Query (value = "SELECT * FROM booking_request where booking_status = 'SCHEDULED' and DATE_FORMAT(date_add(booking_time,interval 10 minute), '%Y%m%d%H%i%s') < DATE_FORMAT(now(), '%Y%m%d%H%i%s');", nativeQuery = true)
	List<BookingRequestEntity> getBookingRequestAfterPassBufferTime();

	@Query (value = "SELECT * from booking_request where fk_charge_request=(?1);", nativeQuery = true)
	BookingRequestEntity getBookingRequestByChargingRequest(String chargingRequestId);

	BookingRequestEntity findByIdAndBookingStatus(String bookingId, String bookingStatus);

	@Query(value = "SELECT DATEDIFF(booking_time, now()) AS date_difference FROM booking_request where id =(?1);", nativeQuery = true)  // by date DateDIFF
	int getDateDiff(String bookingId);
	
	@Query (value = "SELECT TIMESTAMPDIFF(minute,now(),booking_time) AS date_difference FROM booking_request where id =(?1);", nativeQuery = true) // by time TIMEDIFF 
	int getTimeInMinuteDiff(String bookingId);
	
	@Query(value = "SELECT * FROM booking_request where DATEDIFF(booking_time, now()) <= (?1) and booking_status = 'SCHEDULED'  and day_reminder_status = 'N'", nativeQuery = true)  // by date DateDIFF
	List<BookingRequestEntity> oneDayBookingReminderSchedulersByScheduled(int day);
	
	@Query(value = "SELECT * FROM booking_request where TIMESTAMPDIFF(minute,now(),booking_time) <= (?1) and booking_status = 'SCHEDULED'  and hour_reminder_status = 'N'", nativeQuery = true)  // by date DateDIFF
	List<BookingRequestEntity> oneHourBeforeBookingReminderSchedulersByScheduled(int hour);
	
	@Query(value = "SELECT NOW() < (?1)", nativeQuery = true) 
	int givenDateGreaterThanCurrentDate(Instant bookingTime);
	
	
	@Query(value = "select count(id) as count FROM booking_request where booking_status = (?1)  and fk_charging_point = (?2)  and  fk_connector = (?3)   "
			+ " and (date_add((?4),interval 1 second) between booking_time and booking_endtime "
			+ " or date_sub((?5),interval 1 second) between booking_time and booking_endtime)", nativeQuery = true)
	int isBookingSlotIsAvailable(String bookingStatus ,String cp ,String connector , Instant bookingStartTime, Instant bookingEndTime);
			
}

