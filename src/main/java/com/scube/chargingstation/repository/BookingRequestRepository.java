package com.scube.chargingstation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.scube.chargingstation.entity.BookingRequestEntity;

@Repository
public interface BookingRequestRepository extends JpaRepository<BookingRequestEntity, String>{
	
	@Query (value = "SELECT * from booking_request where DATE_FORMAT(booking_time, \"%Y-%m-%d %H:%i:%s\") = (?1) and booking_status = (?2);", nativeQuery = true)
	List<BookingRequestEntity> findByBookingTimeAndBookingStatus(String bookingTime, String bookingStatus);

	List<BookingRequestEntity> findByBookingTime(String bookingTime);
	
	List<BookingRequestEntity> findByBookingStatus( String bookingStatus);

}
