package com.kh.pcar.back.station.model.service;

import java.util.List;

import com.kh.pcar.back.auth.model.vo.CustomUserDetails;
import com.kh.pcar.back.station.model.dto.ReviewDTO;
import com.kh.pcar.back.station.model.dto.StationDTO;

public interface ServiceStation {

	List<StationDTO> stations(String lat, String lng,String stationId);
	List<StationDTO> searchByName(String keyword);	
	int insertReview(ReviewDTO reviewDto);
	int deleteReview(ReviewDTO reviewDto);
	List<ReviewDTO> findAll(String stationId);
	List<StationDTO> searchDetail(Long stationId);
	
}
