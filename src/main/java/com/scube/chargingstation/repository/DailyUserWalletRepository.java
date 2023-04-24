package com.scube.chargingstation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.scube.chargingstation.entity.DailyUserWalletEntity;


@Repository
public interface DailyUserWalletRepository extends JpaRepository<DailyUserWalletEntity, Long> {

	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "INSERT INTO daily_emp_wallet (`id`,`created_at`, `is_deleted`, `modified_at`, `version`, `current_balance`, `fk_user`, `latest_date`) " +
	           "SELECT `id`,`created_at`, `is_deleted`, `modified_at`, `version`, `current_balance`, `fk_user`, NOW() " +
	           "FROM emp_wallet", nativeQuery = true)
	void insertFromEmpWallet();

	
	@Query(value = "select * from daily_emp_wallet where str_to_date(latest_date,\"%Y-%m-%d\") = (?1);", nativeQuery = true)
	List<DailyUserWalletEntity> getLatestDate(String latestdate);

}


