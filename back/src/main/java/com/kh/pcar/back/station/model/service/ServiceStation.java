package com.kh.pcar.back.station.model.service;

import java.util.List;

import com.kh.pcar.back.station.model.dto.MyStationDTO;

public interface ServiceStation {

	List<MyStationDTO> stations(String lat, String lng);
}
