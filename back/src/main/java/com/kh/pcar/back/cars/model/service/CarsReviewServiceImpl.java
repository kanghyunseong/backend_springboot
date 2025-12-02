package com.kh.pcar.back.cars.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kh.pcar.back.auth.model.vo.CustomUserDetails;
import com.kh.pcar.back.cars.model.dao.CarsReviewMapper;
import com.kh.pcar.back.cars.model.dto.CarsReviewDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CarsReviewServiceImpl implements CarsReviewService{
	private final CarsReviewMapper carsReviewMapper;

	@Override
	public List<CarsReviewDTO> findReview(Long carId) {
	
		List<CarsReviewDTO> review = carsReviewMapper.findReview(carId);
		
		return review;
	}

	@Override
	public int insertReview(CarsReviewDTO dto, CustomUserDetails userDetails) {
		
		dto.setReviewWriter(userDetails.getUserNo());
		
		int result = carsReviewMapper.insertReview(dto);
		
		return result;
	}
	
	@Override
	public int updateReview(CarsReviewDTO dto) {

		int result = carsReviewMapper.updateReview(dto);
		
		return result;
	}

	@Override
	public void deleteReview(Long reviewId) {
		
		carsReviewMapper.deleteReview(reviewId);
		
	}

	
}
