package com.scube.chargingstation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.scube.chargingstation.entity.CarModelEntity;
import com.scube.chargingstation.entity.UserCarsEntity;
import com.scube.chargingstation.entity.UserInfoEntity;

@Repository
public interface UserCarsRepository extends JpaRepository<UserCarsEntity, String>  {


	List<UserCarsEntity> findByUserInfoEntity(UserInfoEntity userInfoEntity);
	
	List<UserCarsEntity> findByUserInfoEntityAndIsdeleted(UserInfoEntity userInfoEntity,String IsDeleted);
	
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "delete from emp_cars where id = (?1)", nativeQuery = true)
	int removeUserCar(String userCarId);

}



