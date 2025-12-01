package com.kh.pcar.back.cars.model.service;

import java.util.List;

import com.kh.pcar.back.cars.model.dto.CarsReviewDTO;

public interface CarsReviewService {
	
	List<CarsReviewDTO> findReview(Long carId);

}
