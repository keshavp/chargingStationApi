package com.scube.chargingstation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.scube.chargingstation.entity.UserInfoOtpEntity;

@Repository
public interface UserInfoOtpRepository extends JpaRepository<UserInfoOtpEntity, Long> {


	@Query(value = "SELECT * FROM emp_info_otp where status = 'open' and mobilenumber =(?1)  and otp_code = (?2)  and now() BETWEEN DATE_ADD(created_at , INTERVAL 0 MINUTE) AND DATE_ADD(created_at , INTERVAL 1 MINUTE)   order by created_at limit 1;", nativeQuery = true)
	UserInfoOtpEntity findByMobilenumberAndOtpCodeByOrderByCreatedAt(String mobilenumber, String otp);

	@Query(value = "SELECT * FROM emp_info_otp where status = 'open' and now() > DATE_ADD(created_at , INTERVAL 1 MINUTE)", nativeQuery = true)
	List<UserInfoOtpEntity> findOpenStatusMoreThenOneMinutes();

}
