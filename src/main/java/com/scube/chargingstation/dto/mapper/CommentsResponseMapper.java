package com.scube.chargingstation.dto.mapper;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.scube.chargingstation.dto.CommentsResponseForComplaintsDto;
import com.scube.chargingstation.entity.ComplainResponseEntity;
import com.scube.chargingstation.util.DateUtils;

public class CommentsResponseMapper {
	
	public static CommentsResponseForComplaintsDto toCommentsResponseForComplaintsDto(ComplainResponseEntity complainResponseEntity) {
		
		return new CommentsResponseForComplaintsDto()
				.setComplaintId(complainResponseEntity.getComplaintEntity().getId())
				.setComments(complainResponseEntity.getComments())
				.setCommentDate(DateUtils.formattedInstantToDateTimeString(complainResponseEntity.getCreatedAt()));

	}
	
	public static Set<CommentsResponseForComplaintsDto> toCommentsResponseForComplaintsDtos(Set<ComplainResponseEntity> complainResponseEntities) {
 		
		Set<CommentsResponseForComplaintsDto> commentsResponseForComplaintsDtos = new HashSet<CommentsResponseForComplaintsDto>();
		for(ComplainResponseEntity complainResponseEntity : complainResponseEntities) {
			commentsResponseForComplaintsDtos.add(toCommentsResponseForComplaintsDto(complainResponseEntity)); 
		}
		
        return commentsResponseForComplaintsDtos;
	}
	
}
