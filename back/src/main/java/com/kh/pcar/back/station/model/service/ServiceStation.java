package com.kh.pcar.back.station.model.service;

import java.util.List;

import com.kh.pcar.back.station.model.dto.StationDTO;

public interface ServiceStation {

	List<StationDTO> stations(String lat, String lng);
	List<StationDTO> searchByName(String keyword);
}
