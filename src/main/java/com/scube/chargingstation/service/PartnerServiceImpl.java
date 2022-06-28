package com.scube.chargingstation.service;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.scube.chargingstation.dto.PartnerDto;
import com.scube.chargingstation.dto.incoming.PartnerIncomingDto;
import com.scube.chargingstation.dto.mapper.PartnerMapper;
import com.scube.chargingstation.entity.PartnerInfoEntity;
import com.scube.chargingstation.exception.BRSException;
import com.scube.chargingstation.repository.PartnerRepository;

import ch.qos.logback.classic.Logger;

@Service
public class PartnerServiceImpl implements PartnerService {
	
	
	private static final Logger logger = (Logger) LoggerFactory.getLogger(PartnerServiceImpl.class);   
	
	@Autowired
	PartnerRepository partnerRepository;

	@Override
	public boolean addPartner(@Valid PartnerIncomingDto partnerIncomingDto) {
		// TODO Auto-generated method stub
		
		logger.info("***PartnerServiceImpl addPartner***");
		
		if((partnerIncomingDto.getPartnerName() == "") || (partnerIncomingDto.getPartnerName().trim().isEmpty())) {
			throw BRSException.throwException("Partner Name can't be blank");
		}
		
		if((partnerIncomingDto.getAddress1() == "") || (partnerIncomingDto.getAddress1().trim().isEmpty())) {
			throw BRSException.throwException("Address can't be blank");
		}
		
		if((partnerIncomingDto.getMobileno() == "") || (partnerIncomingDto.getMobileno().trim().isEmpty())) {
			throw BRSException.throwException("Mobile No can't be blank");
		}
		
		if((partnerIncomingDto.getPincode() == "") || (partnerIncomingDto.getPincode().trim().isEmpty())) {
			throw BRSException.throwException("Pin Code can't be blank");
		}
		
		if((partnerIncomingDto.getEmail() == "") || (partnerIncomingDto.getEmail().trim().isEmpty())) {
			throw BRSException.throwException("Email can't be blank");
		}
		
		if((partnerIncomingDto.getGstn() == "") || (partnerIncomingDto.getGstn().trim().isEmpty())) {
			throw BRSException.throwException("GSTN can't be blank");
		}
		
		if((partnerIncomingDto.getStatus() == "") || (partnerIncomingDto.getStatus().trim().isEmpty())) {
			throw BRSException.throwException("Status can't be blank");
		}
			
		logger.info(partnerIncomingDto.getPartnerName());
		
		PartnerInfoEntity partnerInfoEntity = new PartnerInfoEntity();
		partnerInfoEntity.setPartnerName(partnerIncomingDto.getPartnerName());
		partnerInfoEntity.setAddress1(partnerIncomingDto.getAddress1());
		partnerInfoEntity.setAddress2(partnerIncomingDto.getAddress2());
		partnerInfoEntity.setPincode(partnerIncomingDto.getPincode());
		partnerInfoEntity.setMobileno(partnerIncomingDto.getMobileno());
		partnerInfoEntity.setAlternateMobileNo(partnerIncomingDto.getAlternateMobileNo());
		partnerInfoEntity.setEmail(partnerIncomingDto.getEmail());
		partnerInfoEntity.setGstn(partnerIncomingDto.getGstn());
		partnerInfoEntity.setStatus(partnerIncomingDto.getStatus());
		partnerInfoEntity.setIsdeleted("N");
		partnerRepository.save(partnerInfoEntity);
		
		logger.info("Added Partner Successfully");
		
		return true;
	}

	@Override
	public boolean editPartner(@Valid PartnerIncomingDto partnerIncomingDto) {
		// TODO Auto-generated method stub
		
		logger.info("***PartnerServiceImpl editPartner***");
		
		if((partnerIncomingDto.getId() == "") || (partnerIncomingDto.getId().trim().isEmpty())) {
			
			throw BRSException.throwException("Partner ID can't be blank");
			
		}
		
		if((partnerIncomingDto.getPartnerName() == "") || (partnerIncomingDto.getPartnerName().trim().isEmpty())) {
			throw BRSException.throwException("Partner Name can't be blank");
		}
		
		if((partnerIncomingDto.getAddress1() == "") || (partnerIncomingDto.getAddress1().trim().isEmpty())) {
			throw BRSException.throwException("Address can't be blank");
		}
		
		if((partnerIncomingDto.getMobileno() == "") || (partnerIncomingDto.getMobileno().trim().isEmpty())) {
			throw BRSException.throwException("Mobile No can't be blank");
		}
		
		if((partnerIncomingDto.getPincode() == "") || (partnerIncomingDto.getPincode().trim().isEmpty())) {
			throw BRSException.throwException("Pin Code can't be blank");
		}
		
		if((partnerIncomingDto.getEmail() == "") || (partnerIncomingDto.getEmail().trim().isEmpty())) {
			throw BRSException.throwException("Email can't be blank");
		}
		
		if((partnerIncomingDto.getGstn() == "") || (partnerIncomingDto.getGstn().trim().isEmpty())) {
			throw BRSException.throwException("GSTN can't be blank");
		}
		
		PartnerInfoEntity partnerInfoEntity = partnerRepository.findById(partnerIncomingDto.getId()).get();
		
		if(partnerInfoEntity == null) {
			throw BRSException.throwException("Error : Partner ID can't be blank");
		}
		
	//	PartnerInfoEntity partnerInfoEntity = new PartnerInfoEntity();
		partnerInfoEntity.setPartnerName(partnerIncomingDto.getPartnerName());
		partnerInfoEntity.setAddress1(partnerIncomingDto.getAddress1());
		partnerInfoEntity.setAddress2(partnerIncomingDto.getAddress2());
		partnerInfoEntity.setPincode(partnerIncomingDto.getPincode());
		partnerInfoEntity.setMobileno(partnerIncomingDto.getMobileno());
		partnerInfoEntity.setAlternateMobileNo(partnerIncomingDto.getAlternateMobileNo());
		partnerInfoEntity.setEmail(partnerIncomingDto.getEmail());
		partnerInfoEntity.setGstn(partnerIncomingDto.getGstn());
		partnerInfoEntity.setStatus(partnerIncomingDto.getStatus());
		partnerInfoEntity.setIsdeleted("N");
		partnerRepository.save(partnerInfoEntity);
		
		logger.info("Edited Partner Successfully");
		
		return true;
	}
	
	
	@Override
	public List<PartnerDto> getAllPartners() {
		
		logger.info("***PartnerServiceImpl getAllPartners***");
		
		List<PartnerInfoEntity> partnerInfoEntities = partnerRepository.findAll();
		
		return PartnerMapper.toPartnersDtos(partnerInfoEntities);
		
	}
	
	
	@Override
	public PartnerDto getPartnerUserById(String id) {
		
		logger.info("***PartnerServiceImpl getPartnerUserById***");
		
		PartnerInfoEntity partnerInfoEntities = partnerRepository.findById(id).get();
		
		/*
		 * PartnerInfoEntity partnerInfoEntity = new PartnerInfoEntity();
		 * 
		 * if(partnerInfoEntities!=null) partnerInfoEntity=partnerInfoEntities.get();
		 */
		
		if(partnerInfoEntities == null) {
			throw BRSException.throwException("Error : Partner ID can't be blank");
		}
		
		return PartnerMapper.toPartnerDto(partnerInfoEntities);
	}
	
	
	@Override
	public List<PartnerDto> getAllActivePartners() {
		
		logger.info("***PartnerServiceImpl getAllActivePartners***");
		
		List<PartnerInfoEntity> partnerInfoEntities = partnerRepository.findByStatus("ACTIVE");
		
		return PartnerMapper.toPartnersDtos(partnerInfoEntities);
	}

	@Override
	public PartnerInfoEntity getPartnersById(String id) {
		// TODO Auto-generated method stub
		PartnerInfoEntity partnerInfoEntity = partnerRepository.findById(id).get();
			
		return partnerInfoEntity;
	}

	@Override
	public PartnerInfoEntity getPartnerById(String id) {
		// TODO Auto-generated method stub
		PartnerInfoEntity partnerInfoEntity = partnerRepository.findById(id).get();
		
		return partnerInfoEntity;
	}

	@Override
	public boolean deletePartnerUserById(String id) {
		// TODO Auto-generated method stub
		
		PartnerInfoEntity partnerInfoEntities = partnerRepository.findById(id).get();
		
		if(partnerInfoEntities == null) {
			throw BRSException.throwException("Error : Partner ID can't be blank");
		}
		
		partnerRepository.delete(partnerInfoEntities);
		
		return false;
	}

}
