package com.scube.chargingstation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.scube.chargingstation.entity.TransactionsEntity;

@Repository
public interface TransactionsRepository extends JpaRepository<TransactionsEntity, Long>  {

	@Query(value = "SELECT * FROM transactions where StartResult = (?1) order by ChargePointId , ConnectorId ,TransactionId desc ", nativeQuery = true)
	List<TransactionsEntity> findByStartResult(String string);

	TransactionsEntity findByTransactionId(int transactionId);
	
//	TransactionsEntity findByStartResult(String string);

}
