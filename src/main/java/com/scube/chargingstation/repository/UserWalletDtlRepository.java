package com.scube.chargingstation.repository;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.scube.chargingstation.entity.UserInfoEntity;
import com.scube.chargingstation.entity.UserWalletDtlEntity;

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
	
	UserWalletDtlEntity findByOrderId(String OrderId);

	//DATE_FORMAT(trhistory.created_at, '%d %m %Y %r')
	/*
	 * @Query(
	 * value="select trhistory.amount as amount,DATE_FORMAT(trhistory.created_at, '%Y-%m-%d %r') as transactionDate, trhistory.transaction_type as transactionType,trhistory.remark,trhistory.fk_chargingreq as chargingRequestId "
	 * +
	 * " from (SELECT * ,'Added Money'as remark FROM  emp_wallet_dtl ewd where ewd.fk_user =?1 and transaction_type='Credit' and ewd.transaction_id is not null"
	 * + " union " +
	 * "SELECT *,  CASE WHEN ewd.transaction_type='Debit' THEN 'For Charging' WHEN ewd.transaction_type='Credit' THEN 'Refund' ELSE ''  END AS remark"
	 * +
	 * " FROM  emp_wallet_dtl ewd where ewd.fk_user =?1 and ewd.fk_chargingreq is not null) as trhistory "
	 * + " order by created_at desc ",nativeQuery = true)
	 */
	@Query(value="SELECT amount , DATE_FORMAT(created_at, '%Y-%m-%d %T %p') as transactionDate ,transaction_type as transactionType, payment_for as remark, \r\n"
			+ "fk_chargingreq as chargingRequestId , fk_booking_request as bookingRequestId ,transaction_id as transactionId,order_id as orderId FROM emp_wallet_dtl where fk_user=(?1)"
			+ "order by DATE_FORMAT(created_at, '%Y-%m-%d %T %p') desc;",nativeQuery = true)
	List<Map<String, String>> getUserTrHistory(String userId);
	
	
	@Query(value="SELECT * FROM emp_wallet_dtl ew  where ew.transaction_type='Credit' and ew.transaction_id is not null and ew.order_id is not null and ew.fk_user is not null"
			+ " and ew.created_at >= (?1) and  ew.created_at <= (?2) order by ew.created_at desc;",nativeQuery = true)
	List<UserWalletDtlEntity> getAddedMoneyRecordsForAllUsers(String startDate, String endDate);
	
	@Query(value = "SELECT * FROM emp_wallet_dtl where fk_booking_request=(?1) and payment_for = 'Debit - Booking Amount';", nativeQuery = true)
	UserWalletDtlEntity getBookingDetailFromUserWalletDtlEntity(String bookingId);
//	@Query(value="select trhistory.amount as amount,DATE_FORMAT(trhistory.created_at, '%Y-%m-%d %r') as transactionDate, trhistory.transaction_type as transactionType,trhistory.remark,\r\n" + 
//			"trhistory.fk_chargingreq as chargingRequestId \r\n" + 
//			"from (SELECT * ,'Added Money'as remark FROM  emp_wallet_dtl ewd where transaction_type='Credit' and ewd.transaction_id is not null\r\n" + 
//			"union \r\n" + 
//			"SELECT *,  CASE WHEN ewd.transaction_type='Debit' THEN 'For Charging' WHEN ewd.transaction_type='Credit' THEN 'Refund' ELSE ''  END AS remark\r\n" + 
//			"FROM  emp_wallet_dtl ewd where ewd.fk_chargingreq is not null) as trhistory \r\n" + 
//			"order by created_at desc ",nativeQuery = true)
	
	@Query(value="select trhistory.amount as amount,DATE_FORMAT(trhistory.created_at, '%Y-%m-%d %r') as transactionDate, trhistory.transaction_type as transactionType,trhistory.remark,\r\n" + 
			"trhistory.fk_chargingreq as chargingRequestId \r\n" + 
			"from (SELECT * ,'Added Money'as remark FROM  emp_wallet_dtl ewd where transaction_type='Credit' and ewd.transaction_id is not null\r\n" + 
			" union \r\n" + 
			"SELECT *,  CASE WHEN ewd.transaction_type='Debit' THEN 'For Charging' WHEN ewd.transaction_type='Credit' THEN 'Refund' ELSE ''  END AS remark\r\n" + 
			"FROM  emp_wallet_dtl ewd where ewd.fk_chargingreq is not null) as trhistory \r\n" + 
			"order by created_at desc; ",nativeQuery = true)
//	@Query(value = "SELECT * FROM `ocpp.core`.emp_wallet_dtl;",nativeQuery = true)
	List<Map<String, String>> getUserTrHistoryForAllUser();
	
	
}
