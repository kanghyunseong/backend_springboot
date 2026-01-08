package com.kh.pcar.back.cars.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.pcar.back.auth.model.vo.CustomUserDetails;
import com.kh.pcar.back.cars.model.dto.CarsDTO;
import com.kh.pcar.back.cars.model.service.CarsService;
import com.kh.pcar.back.common.ResponseData;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/cars")
public class CarsController {

	private final CarsService carsService;

	// 차량 전체 조회
	@GetMapping
	public ResponseEntity<ResponseData<List<CarsDTO>>> findAll(@RequestParam(value = "page", defaultValue = "0") int pageNo) {
		return ResponseData.ok(carsService.findAll(pageNo));
	}

	// 차량 상세 조회
	// GET /
	@GetMapping("/{carId}")
	public ResponseEntity<ResponseData<List<CarsDTO>>> findByCarId(@PathVariable(name = "carId") Long carId) {
		return ResponseData.ok(carsService.findByCarId(carId));
	}

}
