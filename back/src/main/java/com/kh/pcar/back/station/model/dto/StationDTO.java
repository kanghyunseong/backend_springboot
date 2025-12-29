package com.kh.pcar.back.station.model.dto;

import java.util.Map;

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
	
	public static StationDTO getStation(Map<String, Object> item) {
		String latitude = String.valueOf(item.get("위도"));
		String longitude = String.valueOf(item.get("경도"));
		String stationName = String.valueOf(item.get("충전소명"));
		String address = String.valueOf(item.get("충전소주소"));
		String stationId = String.valueOf(item.get("충전소아이디"));
		String detailAddress = String.valueOf(item.get("상세주소"));
		String tel = String.valueOf(item.get("연락처"));
		String useTime = String.valueOf(item.get("이용가능시간"));
		String regDate = String.valueOf(item.get("등록일자"));

		return new StationDTO(latitude, longitude, stationName, address, stationId, detailAddress, tel, useTime,
				regDate);
	}

}
