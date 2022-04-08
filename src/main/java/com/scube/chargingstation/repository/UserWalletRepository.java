package com.scube.chargingstation.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.scube.chargingstation.entity.UserInfoEntity;
import com.scube.chargingstation.entity.UserWalletEntity;

@Repository
public interface UserWalletRepository extends JpaRepository<UserWalletEntity, Long> {

	/*
	 * UserInfoEntity findByUsername(String username);
	 * 
	 * UserInfoEntity findById(String userId);
	 * 
	 * UserInfoEntity findByMobilenumber(String mobilenumber);
	 */
	
	//SELECT * FROM emp_wallet where fk_user='6b41299bb4' order by created_at desc limit 1

			
			@Query(value = "SELECT * FROM emp_wallet where fk_user=? order by created_at desc limit 1", nativeQuery = true)
			UserWalletEntity findBalanceByUserId(String userId);
			UserWalletEntity findByUserInfoEntity(UserInfoEntity userInfoEntity);
}
