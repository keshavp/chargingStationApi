package com.scube.chargingstation.dto;

import java.util.List;

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
public class AdminDashboardDto {

	private double yesterdayConsumedKwh;
	private int weekNewUserCount;
	private List<MostActiveChargingStationsDto> mostActiveChargingStations;
	private AverageSessionDto averageSession;
	private double WeekConsumedKwh;
	private int weekSessionscount;
	
//	private List<AverageSessionDto> averageSession;
	
}
