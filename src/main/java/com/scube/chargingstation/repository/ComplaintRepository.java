package com.scube.chargingstation.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.scube.chargingstation.entity.ComplaintEntity;
import com.scube.chargingstation.entity.UserInfoEntity;

@Repository
public interface ComplaintRepository extends JpaRepository<ComplaintEntity, String>{
	
	Optional<ComplaintEntity> findById(String id);
	
	@Query (value = "SELECT * FROM emp_complaints where fk_user=(?1) order by created_at desc", nativeQuery = true)
	List<ComplaintEntity> findByUserInfoEntity(UserInfoEntity id);

}
