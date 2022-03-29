package com.scube.chargingstation.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scube.chargingstation.entity.UserInfoEntity;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfoEntity, Long> {

	UserInfoEntity findByUsername(String username);

	UserInfoEntity findById(String userId);

	UserInfoEntity findByMobilenumber(String mobilenumber);

}
