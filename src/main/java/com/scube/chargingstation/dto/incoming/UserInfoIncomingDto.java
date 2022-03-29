package com.scube.chargingstation.dto.incoming;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.UpdateTimestamp;

import com.scube.chargingstation.entity.RoleEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserInfoIncomingDto {
	
	@NotEmpty(message="username cannot be empty.")
	@NotBlank(message="username cannot be blank.")
	private String username;//
	@NotEmpty(message="email cannot be empty.")
	@NotBlank(message="email cannot be blank.")
	private String email;//
	private String mobilenumber;
	private String role;
	private String password;
	private String status;
}
