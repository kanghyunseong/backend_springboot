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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

	private final CarsReviewService carsReviewService;

	@GetMapping("/{carId}") // 차량 리뷰 조회
	public ResponseEntity<List<CarsReviewDTO>> findReview(@PathVariable(name = "carId") Long carId) {

		List<CarsReviewDTO> review = carsReviewService.findReview(carId);

		return ResponseEntity.ok(review);
	}

	@PostMapping // 차량 리뷰 등록
	public ResponseEntity<?> insertReview(@RequestBody CarsReviewDTO dto,
			@AuthenticationPrincipal CustomUserDetails userDetails) {

		carsReviewService.insertReview(dto, userDetails);

		return ResponseEntity.status(HttpStatus.CREATED).body("리뷰 등록 완료");

	}

	@PutMapping //
	public ResponseEntity<?> updateReview(@RequestBody CarsReviewDTO dto,
			@AuthenticationPrincipal CustomUserDetails userDetails) {

		carsReviewService.updateReview(dto);

		return ResponseEntity.ok("리뷰 변경 완료");
	}

	@DeleteMapping("/{reviewNo}") // 차량 리뷰 삭제
	public ResponseEntity<?> deleteReview(@PathVariable(name = "reviewNo") Long reviewNo,
			@AuthenticationPrincipal CustomUserDetails userDetails) {

		carsReviewService.deleteReview(reviewNo);

		return ResponseEntity.ok().body("삭제완료");
	}
}
