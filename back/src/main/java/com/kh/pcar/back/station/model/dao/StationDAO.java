package com.kh.pcar.back.station.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kh.pcar.back.station.model.dto.ReviewDTO;
import com.kh.pcar.back.station.model.dto.StationDTO;

@Mapper
public interface StationDAO {

	int insertReview(ReviewDTO reviewDto);

	int deleteReview(ReviewDTO reviewDto);

	List<ReviewDTO> findAll(String stationId);

	Long searchDetail(Long reviewId);

}
