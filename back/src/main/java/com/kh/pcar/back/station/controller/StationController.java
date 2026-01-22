package com.kh.pcar.back.station.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.pcar.back.auth.model.vo.CustomUserDetails;
import com.kh.pcar.back.common.ResponseData;
import com.kh.pcar.back.station.model.dto.ReviewDTO;

import com.kh.pcar.back.station.model.service.ServiceStation;

//import com.kh.pcar.back.station.model.service.ServiceStation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/station")
@RequiredArgsConstructor
public class StationController {
	private final ServiceStation service;

	@GetMapping
	public ResponseEntity<ResponseData<Object>> stations(@RequestParam(name = "lat") String lat,
			@RequestParam(name = "lng") String lng) {

		//service.stations(lat, lng);

		return ResponseData.created(service.stations(lat, lng));
	}

	@GetMapping("/search")
	public ResponseEntity<ResponseData<Object>> searchStation(@RequestParam(name = "keyword") String keyword) {
		//service.searchStation(keyword);

		return ResponseData.ok(service.searchStation(keyword));

	}

	@GetMapping("/searchDetail/{stationId}")
	public ResponseEntity<ResponseData<Object>> searchDetail(@PathVariable(name = "stationId") Long stationId) {

		//service.searchDetail(stationId);

		return ResponseData.ok(service.searchDetail(stationId));

	}

	@PostMapping("/insert")
	public ResponseEntity<ResponseData<Object>> insertReview(@RequestBody ReviewDTO reviewDto,
			@AuthenticationPrincipal CustomUserDetails userDetails) {

		//service.insertReview(reviewDto, userDetails);

		return ResponseData.created(service.insertReview(reviewDto, userDetails));

	}

	@DeleteMapping
	public ResponseEntity<ResponseData<Object>> deleteReview(@RequestBody ReviewDTO reviewDto,
			@AuthenticationPrincipal CustomUserDetails userDetails) {

		service.deleteReview(reviewDto, userDetails);

		return ResponseData.ok(service.deleteReview(reviewDto, userDetails));

	}

	@GetMapping("/findAll")
	public ResponseEntity<ResponseData<Object>> findAll(@RequestParam(name = "stationId") String stationId

	) {

		service.findAll(stationId);

		return ResponseData.ok(service.findAll(stationId));

	}
}