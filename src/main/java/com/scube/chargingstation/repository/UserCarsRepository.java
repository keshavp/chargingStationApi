package com.scube.chargingstation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scube.chargingstation.entity.CarModelEntity;
import com.scube.chargingstation.entity.UserCarsEntity;
import com.scube.chargingstation.entity.UserInfoEntity;

@Repository
public interface UserCarsRepository extends JpaRepository<UserCarsEntity, String>  {


	List<UserCarsEntity> findByUserInfoEntity(UserInfoEntity userInfoEntity);

}



