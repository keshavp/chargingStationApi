package com.scube.chargingstation.repository;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
	
	
	@Query(value="select trhistory.amount as amount,DATE_FORMAT(trhistory.created_at, '%d %M %Y %r') as transactionDate, trhistory.transaction_type as transactionType,trhistory.remark,trhistory.fk_chargingreq as chargingRequestId "
			+ " from (SELECT * ,'Added Money'as remark FROM  emp_wallet_dtl ewd where ewd.fk_user =?1 and transaction_type='Credit' and ewd.transaction_id is not null"+
			" union " +
			"SELECT *,  CASE WHEN ewd.transaction_type='Debit' THEN 'For Charging' WHEN ewd.transaction_type='Credit' THEN 'Refund' ELSE ''  END AS remark" + 
			 " FROM  emp_wallet_dtl ewd where ewd.fk_user =?1 and ewd.fk_chargingreq is not null) as trhistory "+
			" order by created_at desc ",nativeQuery = true)
	List<Map<String, String>> getUserTrHistory(String userId);
	
	
	
}
