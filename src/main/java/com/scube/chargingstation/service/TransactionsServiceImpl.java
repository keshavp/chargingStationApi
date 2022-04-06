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
import com.scube.chargingstation.entity.ChargingPointEntity;
import com.scube.chargingstation.entity.ChargingRequestEntity;
import com.scube.chargingstation.entity.ConnectorEntity;
import com.scube.chargingstation.entity.TransactionsEntity;
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
		
		logger.info("***TransactionsServiceImpl updateStartResultInitiated***");
		
		for(TransactionsEntity transactionsEntity : transactionsEntitys) {
		
			ChargingPointEntity	chargingPointEntity = chargingPointService.getChargingPointEntityByChargingPointId(transactionsEntity.getChargePointId());
			
			ConnectorEntity	connectorEntity = connectorService.getConnectorEntityByIdAndChargingPointEntity( String.valueOf(transactionsEntity.getConnectorId()) ,chargingPointEntity) ;
			
			ChargingRequestEntity chargingRequestEntity = chargingRequestService.findChargingRequestEntityByChargingPointEntityAndConnectorEntityAndStatus(chargingPointEntity, connectorEntity,"REQUESTED");
			
				if(chargingRequestEntity != null) {
					
					
					//ChargingPointConnectorRateDto	chargingPointConnectorRateDto = chargingPointConnectorRateService.getConnectorByChargingPointNameAndConnectorIdAndAmount(chargingRequestEntity.getChargePointId(),chargingRequestEntity.getConnectorId(),chargingRequestEntity.getRequestAmount());
					
					ChargingPointConnectorRateDto	chargingPointConnectorRateDto = chargingPointConnectorRateService.getConnectorByChargingPointNameAndConnectorIdAndAmount(chargingRequestEntity.getChargingPointEntity().getChargingPointId(),chargingRequestEntity.getConnectorEntity().getConnectorId(),chargingRequestEntity.getRequestAmount());
					
					transactionsEntity.setAllowedCharge(chargingPointConnectorRateDto.getKWh());
					transactionsEntity.setStartResult("Approved");
					transactionsRepository.save(transactionsEntity);
					
					
					chargingRequestEntity.setStatus("Approved");
					chargingRequestEntity.setChargingStatus("Starting");
					chargingRequestEntity.setTransactionsEntity(transactionsEntity);
					chargingRequestRepository.save(chargingRequestEntity);
				}
			
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
				
					
					String filename =  payslipPdfExporter.generatePdf(chargingRequestEntity);
				
					chargingRequestEntity.setInvoiceFilePath(filename);
					
					chargingRequestRepository.save(chargingRequestEntity);
					
					
					
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
