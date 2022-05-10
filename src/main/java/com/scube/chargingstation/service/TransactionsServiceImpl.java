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
import com.scube.chargingstation.util.PayslipPdfExporter;

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
	PayslipPdfExporter	payslipPdfExporter;
	
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
				
				
				if(transactionsEntity.getMeterStop()>0 && transactionsEntity.getStopTime() != null ) {
				
					chargingRequestEntity.setChargingStatus("Done");
					chargingRequestEntity.setStartTime(transactionsEntity.getStartTime());
					chargingRequestEntity.setMeterStart(transactionsEntity.getMeterStart());
					chargingRequestEntity.setStopTime(transactionsEntity.getStopTime());
					chargingRequestEntity.setMeterStop(transactionsEntity.getMeterStop());
				
					String statusCrDr = "";
					double differenceAmount = 0;
					double differenceKwh = 0;
					
					double finalAmount = 0;
					double finalKwh = 0;
					
					
					double minKwh = 0.01;
					ChargingPointConnectorRateDto	chargingPointConnectorRateDto = chargingPointConnectorRateService.getConnectorByChargingPointNameAndConnectorIdAndKwh(chargingRequestEntity.getChargingPointEntity().getChargingPointId(),chargingRequestEntity.getConnectorEntity().getConnectorId(),minKwh);
					
					if(chargingRequestEntity.getRequestAmount() == 0) {
						
						differenceAmount = 0;
						differenceKwh = 0;
						finalAmount = (chargingRequestEntity.getMeterStop() *  chargingPointConnectorRateDto.getAmount())/ minKwh ;
						finalKwh	= chargingRequestEntity.getMeterStop();
					
					}else {
						
						if(chargingRequestEntity.getRequestKwh() == chargingRequestEntity.getMeterStop()) {
							differenceAmount = 0;
							differenceKwh = 0;
							finalAmount = chargingRequestEntity.getRequestAmount();
							finalKwh	= chargingRequestEntity.getMeterStop();
						}else {
						
						
							if(chargingRequestEntity.getRequestKwh() > chargingRequestEntity.getMeterStop()) {
								statusCrDr = "Credit";
								
								differenceKwh = chargingRequestEntity.getRequestKwh() - chargingRequestEntity.getMeterStop();
								
								differenceAmount = (differenceKwh *  chargingPointConnectorRateDto.getAmount())/ minKwh ; // 
								
								finalAmount = chargingRequestEntity.getRequestAmount() - differenceAmount; //
								finalKwh	= chargingRequestEntity.getMeterStop();
							}
		
							if(chargingRequestEntity.getRequestKwh() < chargingRequestEntity.getMeterStop()) {
								statusCrDr = "Debit"; 
								
								differenceKwh = chargingRequestEntity.getMeterStop() - chargingRequestEntity.getRequestKwh();
								
								differenceAmount = (differenceKwh * chargingPointConnectorRateDto.getAmount()) / minKwh; // 
								
								finalAmount = chargingRequestEntity.getRequestAmount() + differenceAmount ; //
								finalKwh	= chargingRequestEntity.getMeterStop();
							}
						
							UserWalletRequestDto	userWalletRequestDto = new UserWalletRequestDto();
							
							userWalletRequestDto.setChargeRequestId(chargingRequestEntity.getId());
							userWalletRequestDto.setTransactionType(statusCrDr);
							userWalletRequestDto.setMobileUser_Id(chargingRequestEntity.getUserInfoEntity().getMobilenumber());
							userWalletRequestDto.setRequestAmount(String.valueOf(differenceAmount));
							
							// Api amount cut in wallet 
							userPaymentService.processWalletMoney(userWalletRequestDto);
							
						}
					}
					
					chargingRequestEntity.setDifferenceAmount(differenceAmount);
					chargingRequestEntity.setDifferenceKwh(differenceKwh);
					chargingRequestEntity.setAmountCrDrStatus(statusCrDr);
					chargingRequestEntity.setFinalAmount(finalAmount);
					chargingRequestEntity.setFinalKwh(finalKwh);
					
					ChargingRequestEntity chargingRequestEntityfilename =  payslipPdfExporter.generatePdf(chargingRequestEntity);
					
					chargingRequestEntity.setInvoiceFilePath(chargingRequestEntityfilename.getInvoiceFilePath());
					chargingRequestEntity.setReceiptNo(chargingRequestEntityfilename.getReceiptNo());
					
					chargingRequestRepository.save(chargingRequestEntity);
					
					//send notification
					Double requestAmt=chargingRequestEntity.getRequestAmount();
					String title="Thank you for using EV-Dock.";
					String body="We are happy to service your request of charging for "+requestAmt+" INR." + 
							"The actual charging consumed is "+finalKwh +" KWH, " + 
							"Corresponding to "+finalAmount + " INR.";
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
