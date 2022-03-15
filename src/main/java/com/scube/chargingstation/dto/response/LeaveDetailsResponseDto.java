package com.scube.chargingstation.dto.response;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LeaveDetailsResponseDto {
	
	public String leavetype;
	public String id;
	public String april;
	public String august;
	public String december;
	public String february;
	public String january;
	public String july;
	public String june;
	public String march;
	public String may;
	public String november;
	public String october;
	public String september;

}
