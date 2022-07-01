package com.scube.chargingstation.service;

import java.util.List;

import javax.validation.Valid;

import com.scube.chargingstation.dto.ComplaintRespDto;
import com.scube.chargingstation.dto.ComplaintResponseCommentsDto;
import com.scube.chargingstation.dto.incoming.ComplaintIncomingDto;

public interface ComplaintService {
	
	boolean addNewComplaint(@Valid ComplaintIncomingDto complaintIncomingDto);
	
	boolean addCommmentForUserComplaint(@Valid ComplaintResponseCommentsDto complaintResponseCommentsDto);
	
	List<ComplaintRespDto> getAllComplaintInfoList();
	
	ComplaintRespDto getComplaintInfoById(String id);
	
	boolean closeComplaint(String id, String userid);
	
	List<ComplaintRespDto> getComplaintDetailsByUserId(String userId);
	
}