package com.kh.pcar.back.cars.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kh.pcar.back.cars.model.dto.CarsReviewDTO;

@Mapper
public interface CarsReviewMapper {
	
	List<CarsReviewDTO>findReview(Long carId);

	int insertReview(CarsReviewDTO dto);
	
	int updateReview(CarsReviewDTO dto);
	
	void deleteReview(Long reviewId);
}
