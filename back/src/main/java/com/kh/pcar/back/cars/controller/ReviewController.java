package com.kh.pcar.back.cars.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.pcar.back.auth.model.vo.CustomUserDetails;
import com.kh.pcar.back.cars.model.dto.CarsReviewDTO;
import com.kh.pcar.back.cars.model.service.CarsReviewService;
import com.kh.pcar.back.common.ResponseData;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

	private final CarsReviewService carsReviewService;

	@GetMapping("/{carId}") // 차량 리뷰 조회
	public ResponseEntity<ResponseData<List<CarsReviewDTO>>> findReview(@PathVariable(name = "carId") Long carId) {
		return ResponseData.ok(carsReviewService.findReview(carId), "차량 리뷰 조회 성공");
	}

	@PostMapping // 차량 리뷰 등록
	public ResponseEntity<ResponseData<Object>> insertReview(@RequestBody CarsReviewDTO dto,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		
		carsReviewService.insertReview(dto, userDetails);
		
		return ResponseData.created("리뷰 등록 성공!");
	}

	@PutMapping //
	public ResponseEntity<ResponseData<Object>> updateReview(@RequestBody CarsReviewDTO dto,
			@AuthenticationPrincipal CustomUserDetails userDetails) {

		carsReviewService.updateReview(dto, userDetails);

		return ResponseData.created("리뷰 변경 완료!");
	}

	@DeleteMapping("/{reviewNo}") // 차량 리뷰 삭제
	public ResponseEntity<ResponseData<Object>> deleteReview(@PathVariable(name = "reviewNo") Long reviewNo,
			@AuthenticationPrincipal CustomUserDetails userDetails) {

		carsReviewService.deleteReview(reviewNo, userDetails);

		return ResponseData.ok("삭제완료");
	}
}
