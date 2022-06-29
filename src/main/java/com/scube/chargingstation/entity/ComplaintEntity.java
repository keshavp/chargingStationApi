package com.scube.chargingstation.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table (name = "emp_complaints")
//@Getter
//@Setter
public class ComplaintEntity extends BaseEntity {
	
	private String userName;
	private String userContactNo;
	
	private String complaintCategory;
	private String complaintDetails;
	
	private String complaintStatus;
	
	@OneToOne
    @JoinColumn(name = "fk_user")
	private UserInfoEntity userInfoEntity;
	
	private String remark;
	
	@OneToOne
    @JoinColumn(name = "close_fk_user")
	private UserInfoEntity closeUserInfoEntity;
	
	@OneToMany(mappedBy = "complaintEntity", cascade = CascadeType.ALL)
	private Set<ComplainResponseEntity> complaintResponseEntity ;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserContactNo() {
		return userContactNo;
	}

	public void setUserContactNo(String userContactNo) {
		this.userContactNo = userContactNo;
	}

	public String getComplaintCategory() {
		return complaintCategory;
	}

	public void setComplaintCategory(String complaintCategory) {
		this.complaintCategory = complaintCategory;
	}

	public String getComplaintDetails() {
		return complaintDetails;
	}

	public void setComplaintDetails(String complaintDetails) {
		this.complaintDetails = complaintDetails;
	}

	public String getComplaintStatus() {
		return complaintStatus;
	}

	public void setComplaintStatus(String complaintStatus) {
		this.complaintStatus = complaintStatus;
	}

	public UserInfoEntity getUserInfoEntity() {
		return userInfoEntity;
	}

	public void setUserInfoEntity(UserInfoEntity userInfoEntity) {
		this.userInfoEntity = userInfoEntity;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


	public UserInfoEntity getCloseUserInfoEntity() {
		return closeUserInfoEntity;
	}

	public void setCloseUserInfoEntity(UserInfoEntity closeUserInfoEntity) {
		this.closeUserInfoEntity = closeUserInfoEntity;
	}

	public Set<ComplainResponseEntity> getComplaintResponseEntity() {
		return complaintResponseEntity;
	}

	public void setComplaintResponseEntity(Set<ComplainResponseEntity> complaintResponseEntity) {
		this.complaintResponseEntity = complaintResponseEntity;
		
		 for(ComplainResponseEntity b : complaintResponseEntity) {
		        b.setComplaintEntity(this);
		    }
	}
	
	
	
}
