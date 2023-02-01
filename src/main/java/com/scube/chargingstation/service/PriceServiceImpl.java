package com.scube.chargingstation.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.scube.chargingstation.dto.ChargingPointConnectorDto;
import com.scube.chargingstation.dto.ChargingPointConnectorRateDto;
import com.scube.chargingstation.dto.ChargingPointDto;
import com.scube.chargingstation.dto.PartnerDto;
import com.scube.chargingstation.dto.PriceDto;
import com.scube.chargingstation.dto.incoming.ChargingPointConnectorRateIncomingDto;
import com.scube.chargingstation.dto.incoming.PriceMasterDto;
import com.scube.chargingstation.dto.mapper.PriceMapper;
import com.scube.chargingstation.entity.ChargingPointConnectorRateEntity;
import com.scube.chargingstation.entity.ChargingPointEntity;
import com.scube.chargingstation.entity.ConnectorEntity;
import com.scube.chargingstation.entity.RoleEntity;
import com.scube.chargingstation.exception.BRSException;
import com.scube.chargingstation.repository.ChargingPointConnectorRateRepository;

@Service
public class PriceServiceImpl implements PriceService{
	
	
	private static final Logger logger = (Logger) LoggerFactory.getLogger(PriceServiceImpl.class); 

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
		
		
		
		ChargingPointEntity chargingPointEntity = chargingPointService.getChargingPointEntityById(chargingPointConnectorRateIncomingDto.getChargingPointId());
		// Cp
		
		
		// Cp + connTy
		ConnectorEntity connectorEntity = connectorService.getConnectorEntityByIdAndChargingPointEntity(chargingPointConnectorRateIncomingDto.getConnectorId(), chargingPointEntity);

		
		
		List<PriceMasterDto> psd=new ArrayList<PriceMasterDto>();
		for(PriceMasterDto priceMasterDtos : chargingPointConnectorRateIncomingDto.getAmount())
		{
			ChargingPointConnectorRateEntity chargingpointconnectorRateEntity = new ChargingPointConnectorRateEntity();
			
			
			chargingpointconnectorRateEntity.setAmount(priceMasterDtos.getAmount());
			chargingpointconnectorRateEntity.setKwh(priceMasterDtos.getKwh());
			chargingpointconnectorRateEntity.setChargingAmount(priceMasterDtos.getChargingAmount());
			chargingpointconnectorRateEntity.setCgst(priceMasterDtos.getCgst());
			chargingpointconnectorRateEntity.setSgst(priceMasterDtos.getSgst());
			chargingpointconnectorRateEntity.setStatus(chargingPointConnectorRateIncomingDto.getStatus());
			chargingpointconnectorRateEntity.setCancelBookingAmount(priceMasterDtos.getCancelBookingAmount());
			
			chargingpointconnectorRateEntity.setConnectorEntity(connectorEntity);
			chargingpointconnectorRateEntity.setChargingPointEntity(chargingPointEntity);
			chargingpointconnectorRateEntity.setIsdeleted("N");

			
			chargingpointconnectorRateEntities.add(chargingpointconnectorRateEntity);
						
		}
		
		// Full Charging Pricing
		ChargingPointConnectorRateEntity chargingpointconnectorRateEntity = new ChargingPointConnectorRateEntity();
			
		chargingpointconnectorRateEntity.setAmount(0);
		chargingpointconnectorRateEntity.setKwh(1000);
		chargingpointconnectorRateEntity.setChargingAmount(0);
		chargingpointconnectorRateEntity.setCgst(0);
		chargingpointconnectorRateEntity.setSgst(0);
		chargingpointconnectorRateEntity.setStatus("ACTIVE");
		chargingpointconnectorRateEntity.setCancelBookingAmount(50);
		
		chargingpointconnectorRateEntity.setConnectorEntity(connectorEntity);
		chargingpointconnectorRateEntity.setChargingPointEntity(chargingPointEntity);
		chargingpointconnectorRateEntity.setIsdeleted("N");
		
		chargingpointconnectorRateEntities.add(chargingpointconnectorRateEntity);
			
		
		chargingPointConnectorRateRepository.saveAll(chargingpointconnectorRateEntities);
			
		return true;
	}


	@Override
	public boolean editPriceRate(@Valid ChargingPointConnectorRateIncomingDto chargingPointConnectorRateIncomingDto) {
		// TODO Auto-generated method stub
		
		List<ChargingPointConnectorRateEntity> chargingpointconnectorRateEntities=new ArrayList< ChargingPointConnectorRateEntity>();
		
		
//		ChargingPointEntity chargingPointEntity = chargingPointService.getChargingPointEntityByChargingPointId(chargingPointConnectorRateIncomingDto.getChargingPointId());
		// Cp
		
		ChargingPointEntity chargingPointEntity = chargingPointService.getChargingPointEntityById(chargingPointConnectorRateIncomingDto.getChargingPointId());
		
		
		// Cp + connTy
		ConnectorEntity connectorEntity = connectorService.getConnectorEntityByIdAndChargingPointEntity(chargingPointConnectorRateIncomingDto.getConnectorId(), chargingPointEntity);

		
		
		List<PriceMasterDto> psd=new ArrayList<PriceMasterDto>();
		for(PriceMasterDto priceMasterDtos : chargingPointConnectorRateIncomingDto.getAmount())
		{
			ChargingPointConnectorRateEntity chargingpointconnectorRateEntity =chargingPointConnectorRateRepository.findById(priceMasterDtos.getId()).get();
			
         
			chargingpointconnectorRateEntity.setAmount(priceMasterDtos.getAmount());
			chargingpointconnectorRateEntity.setChargingAmount(priceMasterDtos.getChargingAmount());
			chargingpointconnectorRateEntity.setCgst(priceMasterDtos.getCgst());
			chargingpointconnectorRateEntity.setKwh(priceMasterDtos.getKwh());
			chargingpointconnectorRateEntity.setSgst(priceMasterDtos.getSgst());
			chargingpointconnectorRateEntity.setStatus(chargingPointConnectorRateIncomingDto.getStatus());
			chargingpointconnectorRateEntity.setCancelBookingAmount(priceMasterDtos.getCancelBookingAmount());
			
			chargingpointconnectorRateEntity.setConnectorEntity(connectorEntity);
			chargingpointconnectorRateEntity.setChargingPointEntity(chargingPointEntity);
			chargingpointconnectorRateEntity.setIsdeleted("N");

			
			chargingpointconnectorRateEntities.add(chargingpointconnectorRateEntity);
		}
		
		
		chargingPointConnectorRateRepository.saveAll(chargingpointconnectorRateEntities);
		
		return true;
	}


	@Override
	public List<PriceDto> getAllPricingDetailsForAllStations() {
		// TODO Auto-generated method stub
		logger.info("***PriceServiceImpl getAllPricingDetailsForAllStations***");
		
		List<ChargingPointConnectorRateEntity> chargingPointConnectorRateEntities = chargingPointConnectorRateRepository.getAllAddedConnectorRateGroupByChargingPointEntityAndConnectEntity();
		
		return PriceMapper.toPriceDtos(chargingPointConnectorRateEntities);
	}


	@Override
	public PriceDto getPricingHistoryById(String pricingId) {
		// TODO Auto-generated method stub
		logger.info("***PriceServiceImpl getPricingDetailsById***");
		
		Optional<ChargingPointConnectorRateEntity> chargingPointConnectorRateEntity = chargingPointConnectorRateRepository.findById(pricingId);
		
		ChargingPointConnectorRateEntity chargingPointConnectorRateEntities = new ChargingPointConnectorRateEntity();
		
		if(chargingPointConnectorRateEntities != null) {
			chargingPointConnectorRateEntities = chargingPointConnectorRateEntity.get();
		}
		
		return PriceMapper.toPriceMasterDto(chargingPointConnectorRateEntities);
	}


	@Override
	public List<PriceDto> getPricingByChargingPointAndConnector(String chargingPoint, String connector) {
		// TODO Auto-generated method stub
		
		List<ChargingPointConnectorRateEntity> chargingPointConnectorRateEntity = chargingPointConnectorRateRepository.getConnectorRateByChargingPointEntityAndConnectEntity(chargingPoint, connector);
		
		return PriceMapper.toPriceDtos(chargingPointConnectorRateEntity);
	}


	@Override
	public boolean deletePrice(String chargingPoint, String connector) {
		// TODO Auto-generated method stub
		List<ChargingPointConnectorRateEntity> chargingPointConnectorRateEntity = chargingPointConnectorRateRepository.getConnectorRateByChargingPointEntityAndConnectEntity(chargingPoint, connector);
		
		if(chargingPointConnectorRateEntity.size() == 0 ) {
			
			throw BRSException.throwException(" can't be blank or null");
		}
		
		chargingPointConnectorRateRepository.deleteAll(chargingPointConnectorRateEntity);
		
		return true;
	}

	
	
}
