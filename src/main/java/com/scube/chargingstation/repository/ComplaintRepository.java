package com.scube.chargingstation.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scube.chargingstation.entity.ComplaintEntity;
import com.scube.chargingstation.entity.UserInfoEntity;

@Repository
public interface ComplaintRepository extends JpaRepository<ComplaintEntity, String>{
	
	Optional<ComplaintEntity> findById(String id);
	
	List<ComplaintEntity> findByUserInfoEntity(UserInfoEntity id);

}
