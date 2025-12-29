package com.kh.pcar.back.cars.model.service;

import java.util.List;

import com.kh.pcar.back.auth.model.vo.CustomUserDetails;
import com.kh.pcar.back.cars.model.dto.CarsReviewDTO;

public interface CarsReviewService {

	List<CarsReviewDTO> findReview(Long carId);

	int insertReview(CarsReviewDTO dto, CustomUserDetails userDetails);

	int updateReview(CarsReviewDTO dto, CustomUserDetails userDetails);

	void deleteReview(Long reviewId, CustomUserDetails userDetails);
}
