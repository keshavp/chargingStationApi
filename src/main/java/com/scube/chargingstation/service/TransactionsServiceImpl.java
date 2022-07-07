package com.scube.chargingstation.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.scube.chargingstation.controller.UserInfoController;
import com.scube.chargingstation.dto.ChargingPointConnectorRateDto;
import com.scube.chargingstation.dto.incoming.NotificationReqDto;
import com.scube.chargingstation.dto.incoming.UserWalletRequestDto;
import com.scube.chargingstation.entity.ChargingPointEntity;
import com.scube.chargingstation.entity.ChargingRequestEntity;
import com.scube.chargingstation.entity.ConnectorEntity;
import com.scube.chargingstation.entity.TransactionsEntity;
import com.scube.chargingstation.exception.BRSException;
import com.scube.chargingstation.repository.ChargingRequestRepository;
import com.scube.chargingstation.repository.TransactionsRepository;
import com.scube.chargingstation.util.ReceiptPdfExporter;
import com.scube.chargingstation.util.Snippet;

@Service
public class TransactionsServiceImpl implements TransactionsService {

	
	private static final Logger logger = LoggerFactory.getLogger(TransactionsServiceImpl.class);
	
	@Autowired
	TransactionsRepository	transactionsRepository;
	
	@Autowired
	ChargingRequestRepository chargingRequestRepository;
	
	@Autowired
	ChargingPointConnectorRateService chargingPointConnectorRateService;
	
	@Autowired
	ChargingRequestService	chargingRequestService;
	
	@Autowired
	ConnectorService	connectorService;
	
	@Autowired
	ChargingPointService	chargingPointService;
	
	@Autowired
	ReceiptPdfExporter	payslipPdfExporter;
	
	@Autowired
	UserPaymentService	userPaymentService;
	
	@Autowired
	NotificationService notificationService;
	
	
	// update status using file 
	/*
	 * @Override public void updateStartResultInitiated() { // TODO Auto-generated
	 * method stub
	 * 
	 * List<TransactionsEntity> transactionsEntitys =
	 * transactionsRepository.findByStartResult("Initiated");
	 * 
	 * logger.info("***TransactionsServiceImpl updateStartResultInitiated***");
	 * 
	 * if(transactionsEntitys.size() > 0) {
	 * 
	 * String fileContent = "";
	 * 
	 * File file; try { file = ResourceUtils.getFile("classpath:data.txt");
	 * 
	 * if(file.exists()) { byte[] fileData = Files.readAllBytes(file.toPath());
	 * fileContent = new String(fileData); }
	 * 
	 * for(TransactionsEntity transactionsEntity : transactionsEntitys) {
	 * 
	 * transactionsEntity.setStartResult(fileContent);
	 * 
	 * transactionsRepository.save(transactionsEntity);
	 * 
	 * }
	 * 
	 * } catch (Exception e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } }
	 * 
	 * }
	 */
	
	// update status using table
	@Override
	public void updateStartResultInitiated() {
		// TODO Auto-generated method stub
		
		List<TransactionsEntity> transactionsEntitys =  transactionsRepository.findByStartResult("Initiated");
		
	//	logger.info("***TransactionsServiceImpl updateStartResultInitiated***");
		
		String oldConnectorId ="";
		String oldChargePointId ="";
		
		for(TransactionsEntity transactionsEntity : transactionsEntitys) {
		
			
			
			ChargingPointEntity	chargingPointEntity = chargingPointService.getChargingPointEntityByChargingPointId(transactionsEntity.getChargePointId());
			
			if(chargingPointEntity == null) {
				//throw BRSException.throwException("Charging Point not found.");
				continue;
			}
			
			ConnectorEntity	connectorEntity = connectorService.getConnectorEntityByIdAndChargingPointEntity( String.valueOf(transactionsEntity.getConnectorId()) ,chargingPointEntity) ;
			
			if(connectorEntity == null) {
				// throw BRSException.throwException("connector not found.");
				continue;
			}
			
			if(!oldConnectorId.equals(connectorEntity.getId()) && !oldChargePointId.equals(chargingPointEntity.getId())) {
			
			ChargingRequestEntity chargingRequestEntity = chargingRequestService.findChargingRequestEntityByChargingPointEntityAndConnectorEntityAndStatus(chargingPointEntity, connectorEntity,"REQUESTED");
			
				if(chargingRequestEntity != null) {
					
					UserWalletRequestDto	userWalletRequestDto = new UserWalletRequestDto();
					
					//ChargingPointConnectorRateDto	chargingPointConnectorRateDto = chargingPointConnectorRateService.getConnectorByChargingPointNameAndConnectorIdAndAmount(chargingRequestEntity.getChargePointId(),chargingRequestEntity.getConnectorId(),chargingRequestEntity.getRequestAmount());
					
					ChargingPointConnectorRateDto	chargingPointConnectorRateDto = chargingPointConnectorRateService.getConnectorByChargingPointNameAndConnectorIdAndAmount(chargingRequestEntity.getChargingPointEntity().getChargingPointId(),chargingRequestEntity.getConnectorEntity().getConnectorId(),chargingRequestEntity.getRequestAmount());
					
					userWalletRequestDto.setChargeRequestId(chargingRequestEntity.getId());
					userWalletRequestDto.setTransactionType("Debit");
					userWalletRequestDto.setMobileUser_Id(chargingRequestEntity.getUserInfoEntity().getMobilenumber());
					userWalletRequestDto.setRequestAmount(String.valueOf(chargingRequestEntity.getRequestAmount()));
					
					// Api amount cut in wallet 
					userPaymentService.processWalletMoney(userWalletRequestDto);
					
					transactionsEntity.setAllowedCharge(chargingPointConnectorRateDto.getKWh());
					transactionsEntity.setStartResult("Approved");
					transactionsRepository.save(transactionsEntity);
					
					chargingRequestEntity.setRequestKwh(chargingPointConnectorRateDto.getKWh());
					chargingRequestEntity.setStatus("Approved");
					chargingRequestEntity.setChargingStatus("Starting");
					chargingRequestEntity.setTransactionsEntity(transactionsEntity);
					chargingRequestRepository.save(chargingRequestEntity);
				}
				
			}
			
				oldConnectorId = connectorEntity.getId();
				oldChargePointId = chargingPointEntity.getId();
				
				
		}
	}
	
	
	
	@Override
	public void chargingRequestedBill() throws Exception {
		// TODO Auto-generated method stub
		
		List<ChargingRequestEntity> chargingRequestEntities = chargingRequestService.findChargingRequestEntityByChargingStatus("Starting");
		
		
		for(ChargingRequestEntity chargingRequestEntity : chargingRequestEntities) {
		
			if(chargingRequestEntity != null) {
				
				
				TransactionsEntity transactionsEntity =  transactionsRepository.findByTransactionId(chargingRequestEntity.getTransactionsEntity().getTransactionId());
				
				
//				if(transactionsEntity.getMeterStop()>0 && transactionsEntity.getStopTime() != null ) {

				if( transactionsEntity.getStopTime() != null ) {
				
					chargingRequestEntity.setChargingStatus("Done");
					chargingRequestEntity.setStartTime(transactionsEntity.getStartTime());
					chargingRequestEntity.setMeterStart(transactionsEntity.getMeterStart());
					chargingRequestEntity.setStopTime(transactionsEntity.getStopTime());
					logger.info("***1***");

					String chargingTime = Snippet.twoInstantDifference(transactionsEntity.getStartTime(), transactionsEntity.getStopTime());
					
					double chargingKwh = 0;
					
					logger.info("***11***");

					if(transactionsEntity.getStopReason().equals("PowerLoss")) {
						chargingRequestEntity.setMeterStop(transactionsEntity.getLastMeter());
						
						 chargingKwh = transactionsEntity.getLastMeter() - transactionsEntity.getMeterStart();
						
					}else {
						chargingRequestEntity.setMeterStop(transactionsEntity.getMeterStop());	
						
						 chargingKwh = transactionsEntity.getMeterStop() - transactionsEntity.getMeterStart();
							logger.info("***111***");

					}
					
					
				
					String statusCrDr = "";
					double differenceAmount = 0;
					double differenceKwh = 0;
					
					double finalAmount = 0;
					double finalKwh = 0;
					
					  
				//	double minKwh = 0.01;   
					//.getConnectorId()
				//	ChargingPointConnectorRateDto	chargingPointConnectorRateDto = chargingPointConnectorRateService.getConnectorByChargingPointNameAndConnectorIdAndKwh(chargingRequestEntity.getChargingPointEntity().getChargingPointId(),chargingRequestEntity.getConnectorEntity().getId(),minKwh);
					logger.info("***1111***");

					double oneKwh = 1;   
					//.getConnectorId()
					ChargingPointConnectorRateDto	oneKwhchargingPointConnectorRateDto = chargingPointConnectorRateService.getConnectorByChargingPointNameAndConnectorIdAndKwh(chargingRequestEntity.getChargingPointEntity().getChargingPointId(),chargingRequestEntity.getConnectorEntity().getId(),oneKwh);
					
					
					
					if(chargingRequestEntity.getRequestAmount() == 0) {
						
						// old 0.01 kwh
						// differenceAmount = (chargingKwh *  chargingPointConnectorRateDto.getAmount())/ minKwh;
						
						differenceAmount = (chargingKwh *  oneKwhchargingPointConnectorRateDto.getAmount())/ oneKwh;
						differenceKwh = chargingKwh;
						statusCrDr = "Debit"; 
						// old 0.01 kwh
						// finalAmount = (chargingKwh *  chargingPointConnectorRateDto.getAmount())/ minKwh ;
						
						finalAmount = (chargingKwh *  oneKwhchargingPointConnectorRateDto.getAmount())/ oneKwh ;
						finalKwh	= chargingKwh;
						
						/*
						 * UserWalletRequestDto userWalletRequestDto = new UserWalletRequestDto();
						 * 
						 * userWalletRequestDto.setChargeRequestId(chargingRequestEntity.getId());
						 * userWalletRequestDto.setTransactionType(statusCrDr);
						 * userWalletRequestDto.setMobileUser_Id(chargingRequestEntity.getUserInfoEntity
						 * ().getMobilenumber());
						 * userWalletRequestDto.setRequestAmount(String.valueOf(differenceAmount));
						 * 
						 * // Api amount cut in wallet
						 * userPaymentService.processWalletMoney(userWalletRequestDto);
						 * 
						 * logger.info("***11111***");
						 */

					
					}else {
						logger.info("***111111***");
						if(chargingRequestEntity.getRequestKwh() == chargingKwh) {
							differenceAmount = 0;
							differenceKwh = 0;
							finalAmount = chargingRequestEntity.getRequestAmount();
							finalKwh	= chargingKwh;
						}else {
						
							logger.info("***1111111***");
							if(chargingRequestEntity.getRequestKwh() > chargingKwh) {
								statusCrDr = "Credit";
								
								differenceKwh = chargingRequestEntity.getRequestKwh() - chargingKwh;
								
								// old 0.01 kwh
								//differenceAmount = (differenceKwh *  chargingPointConnectorRateDto.getAmount())/ minKwh ; //
								
								differenceAmount = (differenceKwh *  oneKwhchargingPointConnectorRateDto.getAmount())/ oneKwh ; // 
								
								finalAmount = chargingRequestEntity.getRequestAmount() - differenceAmount; //
								finalKwh	= chargingKwh;
								logger.info("***2***");

							}
		
							if(chargingRequestEntity.getRequestKwh() < chargingKwh) {
								logger.info("***3***");

								statusCrDr = "Debit"; 
								
								differenceKwh = chargingKwh - chargingRequestEntity.getRequestKwh();
								// old 0.01 kwh
								// differenceAmount = (differenceKwh * chargingPointConnectorRateDto.getAmount()) / minKwh; //
								
								differenceAmount = (differenceKwh * oneKwhchargingPointConnectorRateDto.getAmount()) / oneKwh; // 
								
								finalAmount = chargingRequestEntity.getRequestAmount() + differenceAmount ; //
								finalKwh	= chargingKwh;
								logger.info("***4***");

							}
						
							/*
							 * UserWalletRequestDto userWalletRequestDto = new UserWalletRequestDto();
							 * 
							 * userWalletRequestDto.setChargeRequestId(chargingRequestEntity.getId());
							 * userWalletRequestDto.setTransactionType(statusCrDr);
							 * userWalletRequestDto.setMobileUser_Id(chargingRequestEntity.getUserInfoEntity
							 * ().getMobilenumber());
							 * userWalletRequestDto.setRequestAmount(String.valueOf(differenceAmount));
							 * 
							 * // Api amount cut in wallet
							 * userPaymentService.processWalletMoney(userWalletRequestDto);
							 * logger.info("***5***");
							 */

							
						}
					}
					
					chargingRequestEntity.setDifferenceAmount(differenceAmount);
					chargingRequestEntity.setDifferenceKwh(differenceKwh);
					chargingRequestEntity.setAmountCrDrStatus(statusCrDr);
					chargingRequestEntity.setFinalAmount(finalAmount);
					chargingRequestEntity.setFinalKwh(finalKwh);
					chargingRequestEntity.setChargingTime(chargingTime);
					
					ChargingRequestEntity chargingRequestEntityfilename =  payslipPdfExporter.generatePdf(chargingRequestEntity , oneKwhchargingPointConnectorRateDto);
					logger.info("***6***");

					chargingRequestEntity.setInvoiceFilePath(chargingRequestEntityfilename.getInvoiceFilePath());
					chargingRequestEntity.setReceiptNo(chargingRequestEntityfilename.getReceiptNo());
					
					chargingRequestRepository.save(chargingRequestEntity);
					
					
					
					UserWalletRequestDto	userWalletRequestDto = new UserWalletRequestDto();
					
					userWalletRequestDto.setChargeRequestId(chargingRequestEntity.getId());
					userWalletRequestDto.setTransactionType(chargingRequestEntity.getAmountCrDrStatus());
					userWalletRequestDto.setMobileUser_Id(chargingRequestEntity.getUserInfoEntity().getMobilenumber());
					userWalletRequestDto.setRequestAmount(String.valueOf(chargingRequestEntity.getDifferenceAmount()));
					
					userPaymentService.processWalletMoney(userWalletRequestDto);
					
					logger.info("***7***");

					
					//send notification
					Double requestAmt=chargingRequestEntity.getRequestAmount();
					String title="Thank you for using EV-Dock.";
					String body="We are happy to service your request of charging for "+requestAmt+" INR." + 
							"The actual charging consumed is "+String.format("%.3f", finalKwh) +" KWH, " + 
							"Corresponding to "+String.format("%.2f", finalAmount) + " INR.";
					NotificationReqDto notificationReqDto =new NotificationReqDto();
					notificationReqDto.setMobileUser_Id(chargingRequestEntity.getUserInfoEntity().getMobilenumber());
					notificationReqDto.setTitle(title);
					notificationReqDto.setBody(body);
					notificationService.sendNotification(notificationReqDto);
					//notification sent
					
				}
			}
		}
	}
	
	@Override
	public void updateStartResultInitiated1() {
		// TODO Auto-generated method stub
		
		List<TransactionsEntity> transactionsEntitys =  transactionsRepository.findByStartResult("Initiated");
		
		logger.info("***TransactionsServiceImpl updateStartResultInitiated***");
		
		if(transactionsEntitys.size() > 0) {
		
			for(TransactionsEntity transactionsEntity : transactionsEntitys) {
			
					if(transactionsEntity.getConnectorId() == 1) {
						
						transactionsEntity.setStartResult("Rejected");
						
					}else {
					
						transactionsEntity.setStartResult("Approved");
					}
					
					logger.info("***UserInfoController addUser***",transactionsEntity.getStartResult());
					
					transactionsRepository.save(transactionsEntity);
			}
		}
	}
}
