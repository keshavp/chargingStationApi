package com.scube.chargingstation.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.scube.chargingstation.dto.ComplaintRespDto;
import com.scube.chargingstation.dto.ComplaintResponseCommentsDto;
import com.scube.chargingstation.dto.incoming.ComplaintIncomingDto;
import com.scube.chargingstation.dto.mapper.ComplaintMapper;
import com.scube.chargingstation.entity.ComplainResponseEntity;
import com.scube.chargingstation.entity.ComplaintEntity;
import com.scube.chargingstation.entity.UserInfoEntity;
import com.scube.chargingstation.exception.BRSException;
import com.scube.chargingstation.repository.ComplaintRepository;
import com.scube.chargingstation.repository.UserInfoRepository;

import ch.qos.logback.classic.Logger;

@Service
public class ComplaintServiceImpl implements ComplaintService {
	
	
	private static final Logger logger = (Logger) LoggerFactory.getLogger(ComplaintServiceImpl.class);
	
	@Autowired
	ComplaintRepository complaintRepository;
	
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	UserInfoRepository userInfoRepository;
	
	@Override
	public boolean addNewComplaint(@Valid ComplaintIncomingDto complaintIncomingDto) {
		// TODO Auto-generated method stub
		logger.info("********ComplaintServiceImpl addNewComplaint********");
		
		if(complaintIncomingDto.getUserName() == "" || complaintIncomingDto.getUserName().trim().isEmpty()) {
			
			throw BRSException.throwException("Error : User name can't be blank");
			
		}
		
		if(complaintIncomingDto.getUserContactNo() == "" || complaintIncomingDto.getUserContactNo().trim().isEmpty()) {
			
			throw BRSException.throwException("Error : User Contact No can't be blank");
			
		}

		if(complaintIncomingDto.getComplaintCategory() == "" || complaintIncomingDto.getComplaintCategory().trim().isEmpty()) {
	
			throw BRSException.throwException("Error : Complaint Category can't be blank");
	
		}

		if(complaintIncomingDto.getComplaintDetail() == "" || complaintIncomingDto.getComplaintDetail().trim().isEmpty()) {
	
			throw BRSException.throwException("Error : Complaint Details can't be blank");
	
		}
		
		UserInfoEntity userInfoEntity = userInfoService.getUserByMobilenumber(complaintIncomingDto.getUserContactNo());
		
		ComplaintEntity complaintEntity = new ComplaintEntity();
		complaintEntity.setUserName(complaintIncomingDto.getUserName());
		complaintEntity.setUserInfoEntity(userInfoEntity);
		complaintEntity.setUserContactNo(complaintIncomingDto.getUserContactNo());
		complaintEntity.setComplaintCategory(complaintIncomingDto.getComplaintCategory());
		complaintEntity.setComplaintDetails(complaintIncomingDto.getComplaintDetail());
		complaintEntity.setComplaintStatus("OPEN");
		complaintEntity.setIsdeleted("N");
		complaintEntity.setRemark("OPEN");
//		complaintEntity.setUserInfoEntity(userInfoEntity);
		
		complaintRepository.save(complaintEntity);
		
		return true;
	}

	@Override
	public List<ComplaintRespDto> getAllComplaintInfoList() {
		// TODO Auto-generated method stub
		logger.info("********ComplaintServiceImpl getAllComplaintInfoList********");
		
		List<ComplaintEntity> complaintEntities = complaintRepository.findAll();
		
		return ComplaintMapper.tComplaintRespDtos(complaintEntities);
	}

	@Override
	public boolean addCommmentForUserComplaint(@Valid ComplaintResponseCommentsDto complaintResponseCommentsDto) {
		// TODO Auto-generated method stub
		
		logger.info("********ComplaintServiceImpl addCommmentForUserComplaint********");
		
		UserInfoEntity userInfoEntity = userInfoService.getUserByMobilenumber(complaintResponseCommentsDto.getUserMobileNo());
		
		ComplaintEntity complaintEntity = complaintRepository.findById(complaintResponseCommentsDto.getComplaintId()).get();
		
		Set<ComplainResponseEntity> complainResponseEntities = new HashSet<ComplainResponseEntity>();
		
		 /* 
		 * // Set<ComplainResponseEntity> complaintResponseEntity = new
		 * HashSet<ComplainResponseEntity>();
		 * 
		 * logger.info("Entity :- " + complaintEntity);
		 * 
		 * // complaintEntity.setComplaintResponseEntity(complaintEntity.
		 * getComplaintResponseEntity());
		 * 
		 * complaintRepository.save(complaintEntity);
		 */
//		Set<ComplaintResponseCommentsDto> commentsDtos = complaintResponseCommentsDto.getComplaintId();
		
//		for(ComplaintResponseCommentsDto complaintResponseCommentsDtos : commentsDtos) {
			
			ComplainResponseEntity complainResponseEntity = new ComplainResponseEntity();
			
		//	complainResponseEntity.setComplaintEntity(complaintResponseCommentsDtos.getComplaintId());
			complainResponseEntity.setComments(complaintResponseCommentsDto.getComments());
			complainResponseEntity.setIsdeleted("N");
			complainResponseEntity.setCloseUserEntity(userInfoEntity);
			complainResponseEntities.add(complainResponseEntity);
		

		
		complaintEntity.setComplaintResponseEntity(complainResponseEntities);
		
		
		complaintRepository.save(complaintEntity);
		
		return true;
	}

	@Override
	public ComplaintRespDto getComplaintInfoById(String id) {
		// TODO Auto-generated method stub
		
		logger.info("********ComplaintServiceImpl getComplaintInfoById********");
		
		ComplaintEntity complaintEntity = complaintRepository.findById(id).get();
		
		logger.info("Complaint Info ID :- " + id);
		
		return ComplaintMapper.tComplaintRespDto(complaintEntity);
	}

	@Override
	public boolean closeComplaint(String id , String userMobileNo) {
		// TODO Auto-generated method stub
		
		logger.info("********ComplaintServiceImpl closeComplaint********");
		
		UserInfoEntity userInfoEntity = userInfoService.getUserByMobilenumber(userMobileNo);
		
		
		ComplaintEntity complaintEntity = complaintRepository.findById(id).get();
		
		complaintEntity.setRemark("CLOSE");
		complaintEntity.setComplaintStatus("CLOSE");
		complaintEntity.setIsdeleted("Y");
		complaintEntity.setCloseUserInfoEntity(userInfoEntity);
		
		complaintRepository.save(complaintEntity);
		
		return true;
	}

	@Override
	public List<ComplaintRespDto> getComplaintDetailsByUserId(String userMobileNo) {
		// TODO Auto-generated method stub
		
		logger.info("********ComplaintServiceImpl getComplaintDetailsByUserId********");
		
		UserInfoEntity userInfoEntity = userInfoService.getUserByMobilenumber(userMobileNo);
		
		List<ComplaintEntity> complaintEntity = complaintRepository.findByUserInfoEntity(userInfoEntity);
		
		return ComplaintMapper.tComplaintRespDtos(complaintEntity);
	}

}
