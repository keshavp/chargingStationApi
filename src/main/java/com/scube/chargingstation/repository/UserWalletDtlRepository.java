package com.scube.chargingstation.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scube.chargingstation.entity.UserInfoEntity;
import com.scube.chargingstation.entity.UserWalletDtlEntity;
import com.scube.chargingstation.entity.UserWalletEntity;

@Repository
public interface UserWalletDtlRepository extends JpaRepository<UserWalletDtlEntity, Long> {

	/*
	 * UserInfoEntity findByUsername(String username);
	 * 
	 * UserInfoEntity findById(String userId);
	 * 
	 * UserInfoEntity findByMobilenumber(String mobilenumber);
	 */
	
	UserWalletDtlEntity findByUserInfoEntityAndOrderId(UserInfoEntity userInfoEntity,String OrderId);
}
