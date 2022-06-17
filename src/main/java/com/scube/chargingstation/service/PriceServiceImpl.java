package com.scube.chargingstation.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scube.chargingstation.dto.ChargingPointConnectorDto;
import com.scube.chargingstation.dto.ChargingPointConnectorRateDto;
import com.scube.chargingstation.dto.ChargingPointDto;
import com.scube.chargingstation.dto.incoming.ChargingPointConnectorRateIncomingDto;
import com.scube.chargingstation.dto.incoming.PriceMasterDto;
import com.scube.chargingstation.entity.ChargingPointConnectorRateEntity;
import com.scube.chargingstation.entity.ChargingPointEntity;
import com.scube.chargingstation.entity.ConnectorEntity;
import com.scube.chargingstation.repository.ChargingPointConnectorRateRepository;

@Service
public class PriceServiceImpl implements PriceService{

	@Autowired
	ChargingPointConnectorRateRepository chargingPointConnectorRateRepository;
	
	@Autowired
	ConnectorService	connectorService;
	
	@Autowired
	ChargingPointService	chargingPointService;

	
	@Override
	public boolean addPrice(@Valid ChargingPointConnectorRateIncomingDto chargingPointConnectorRateIncomingDto) {
		// TODO Auto-generated method stub
		
		
		
		ChargingPointConnectorRateEntity chargingpointconnectorRateEntity=new ChargingPointConnectorRateEntity();
		
		//chargingpointconnectorRateEntity.setChargingPointEntity(chargingPointConnectorRateIncomingDto.getConnectorEntity());
	//	chargingpointconnectorRateEntity.setConnectorEntity(chargingPointConnectorRateIncomingDto.getChargingPointEntity());
	//	chargingpointconnectorRateEntity.setAmount(chargingPointConnectorRateIncomingDto.getAmount());
	//	chargingpointconnectorRateEntity.setKwh(chargingPointConnectorRateIncomingDto.getKwh());
		chargingpointconnectorRateEntity.setIsdeleted("N");
		chargingPointConnectorRateRepository.save(chargingpointconnectorRateEntity);
		
		return true;
	}


	@Override
	public boolean addPriceRate(@Valid ChargingPointConnectorRateIncomingDto chargingPointConnectorRateIncomingDto) {

		// TODO Auto-generated method stub
		
		List<ChargingPointConnectorRateEntity> chargingpointconnectorRateEntities=new ArrayList< ChargingPointConnectorRateEntity>();
		
		
		
		ChargingPointEntity chargingPointEntity = chargingPointService.getChargingPointEntityByChargingPointId(chargingPointConnectorRateIncomingDto.getChargingPointId());
		// Cp
		
		
		// Cp + connTy
		ConnectorEntity connectorEntity = connectorService.getConnectorEntityByIdAndChargingPointEntity(chargingPointConnectorRateIncomingDto.getConnectorId(), chargingPointEntity);

		
		
		List<PriceMasterDto> psd=new ArrayList<PriceMasterDto>();
		for(PriceMasterDto priceMasterDtos : chargingPointConnectorRateIncomingDto.getAmount())
		{
			ChargingPointConnectorRateEntity chargingpointconnectorRateEntity =new ChargingPointConnectorRateEntity();
			
			
			
			
			chargingpointconnectorRateEntity.setAmount(priceMasterDtos.getAmount());
			chargingpointconnectorRateEntity.setChargingAmount(priceMasterDtos.getChargingAmount());
			chargingpointconnectorRateEntity.setCgst(priceMasterDtos.getCgst());
			chargingpointconnectorRateEntity.setKwh(priceMasterDtos.getKwh());
			chargingpointconnectorRateEntity.setSgst(priceMasterDtos.getSgst());
		
			
			chargingpointconnectorRateEntity.setConnectorEntity(connectorEntity);
			chargingpointconnectorRateEntity.setChargingPointEntity(chargingPointEntity);
			chargingpointconnectorRateEntity.setIsdeleted("N");

			
			chargingpointconnectorRateEntities.add(chargingpointconnectorRateEntity);
						
		}
		
			
			chargingPointConnectorRateRepository.saveAll(chargingpointconnectorRateEntities);
			
		return true;
	}


	@Override
	public boolean editPriceRate(@Valid ChargingPointConnectorRateIncomingDto chargingPointConnectorRateIncomingDto) {
		// TODO Auto-generated method stub
		
List<ChargingPointConnectorRateEntity> chargingpointconnectorRateEntities=new ArrayList< ChargingPointConnectorRateEntity>();
		
		
		
		ChargingPointEntity chargingPointEntity = chargingPointService.getChargingPointEntityByChargingPointId(chargingPointConnectorRateIncomingDto.getChargingPointId());
		// Cp
		
		
		// Cp + connTy
		ConnectorEntity connectorEntity = connectorService.getConnectorEntityByIdAndChargingPointEntity(chargingPointConnectorRateIncomingDto.getConnectorId(), chargingPointEntity);

		
		
		List<PriceMasterDto> psd=new ArrayList<PriceMasterDto>();
		for(PriceMasterDto priceMasterDtos : chargingPointConnectorRateIncomingDto.getAmount())
		{
			ChargingPointConnectorRateEntity chargingpointconnectorRateEntity =chargingPointConnectorRateRepository.findById(chargingPointConnectorRateIncomingDto.getId()).get();
			
         
			chargingpointconnectorRateEntity.setAmount(priceMasterDtos.getAmount());
			chargingpointconnectorRateEntity.setChargingAmount(priceMasterDtos.getChargingAmount());
			chargingpointconnectorRateEntity.setCgst(priceMasterDtos.getCgst());
			chargingpointconnectorRateEntity.setKwh(priceMasterDtos.getKwh());
			chargingpointconnectorRateEntity.setSgst(priceMasterDtos.getSgst());
		
			
			chargingpointconnectorRateEntity.setConnectorEntity(connectorEntity);
			chargingpointconnectorRateEntity.setChargingPointEntity(chargingPointEntity);
			chargingpointconnectorRateEntity.setIsdeleted("N");

			
			chargingpointconnectorRateEntities.add(chargingpointconnectorRateEntity);
		}
		
		
		chargingPointConnectorRateRepository.saveAll(chargingpointconnectorRateEntities);
		
		return true;
	}

	
	
}
