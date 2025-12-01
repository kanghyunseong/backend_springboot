package com.kh.pcar.back.station.model.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class StationDTO {
	
	private String lat;
	private String lng;
	private String stationName;
	private String address;
	private String stationId;
	private String detailAddress;
	private String tel;
	private String  useTime;
	private String regDate;

}
