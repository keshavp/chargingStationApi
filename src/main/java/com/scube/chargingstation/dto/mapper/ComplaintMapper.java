package com.scube.chargingstation.dto.mapper;

import java.util.ArrayList;
import java.util.List;

import com.scube.chargingstation.dto.ComplaintRespDto;
import com.scube.chargingstation.entity.ComplaintEntity;
import com.scube.chargingstation.util.DateUtils;

public class ComplaintMapper {
	
	public static ComplaintRespDto toComplaintRespDto(ComplaintEntity complaintEntity) {
		
		return new ComplaintRespDto()
				.setComplaintId(complaintEntity.getId())
				.setComplaintCategory(complaintEntity.getComplaintCategory())
				.setComplaintDetail(complaintEntity.getComplaintDetails())
				.setUserName(complaintEntity.getUserName())
				.setUserContactNo(complaintEntity.getUserContactNo())
				.setComplaintStatus(complaintEntity.getComplaintStatus())
				.setCommentsResponse(CommentsResponseMapper.toCommentsResponseForComplaintsDtos(complaintEntity.getComplaintResponseEntity()))
				.setComplaintDate(DateUtils.formattedInstantToSimpleDateTimeFormat(complaintEntity.getCreatedAt()))
				.setComplaintCloseDate(DateUtils.formattedInstantToSimpleDateTimeFormat(complaintEntity.getComplaintCloseDate()));
		
	}
	
	
	public static List<ComplaintRespDto> toComplaintRespDtos(List<ComplaintEntity> complaintEntities) {
 		
		List<ComplaintRespDto> complaintRespDtos = new ArrayList<ComplaintRespDto>();
		
		for(ComplaintEntity complaintEntity : complaintEntities) {
			
			complaintRespDtos.add(toComplaintRespDto(complaintEntity)); 
			
		}
		
        return complaintRespDtos;
	}

}
