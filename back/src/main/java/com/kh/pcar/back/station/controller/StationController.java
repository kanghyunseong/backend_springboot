package com.kh.pcar.back.station.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
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
import com.kh.pcar.back.station.model.dto.ReviewDTO;
import com.kh.pcar.back.station.model.dto.StationDTO;
import com.kh.pcar.back.station.model.service.ServiceStation;

//import com.kh.pcar.back.station.model.service.ServiceStation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/station")
@RequiredArgsConstructor
public class StationController {
	private final ServiceStation service;

	// 서비스로 사용자의 위치 정보 전송 그 후 걸러낸걸 MyStationDTO에 담음
	@GetMapping
	public List<StationDTO> stations(@RequestParam(name = "lat") String lat, @RequestParam(name = "lng") String lng,
			@RequestParam(name = "stationId", required = false) String stationId) {
//		log.info("{}",page);
//		log.info("{}",serviceKey);
//		log.info("{}",perPage);
//		log.info("{}",stationId);
//		//현재 위치
//		log.info("{},{}",lat,lng);
		List<StationDTO> stations = service.stations(lat, lng, stationId);
//		log.info("{}",stations);
		return stations;
	}

	@GetMapping("/search")
	public List<StationDTO> searchStation(@RequestParam(name = "keyword") String keyword) {
		List<StationDTO> result = service.searchByName(keyword);
//		log.info("{}",result);		
		return result;
	}

	@GetMapping("/searchDetail/{stationId}")
	public List<StationDTO> searchDetail(@PathVariable(name = "stationId") Long stationId) {

		List<StationDTO> result = service.searchDetail(stationId);
		log.info("{}", result);
		return result;

	}

	@PostMapping("/insert")
	public ResponseEntity<String> insertReview(@RequestBody ReviewDTO reviewDto,
			@AuthenticationPrincipal CustomUserDetails userDetails) {

		int result = service.insertReview(reviewDto, userDetails);

		return ResponseEntity.ok().body("리뷰등록성공");

	}

	@DeleteMapping
	public ResponseEntity<String> deleteReview(@RequestBody ReviewDTO reviewDto,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		log.error("{}", userDetails);
		service.deleteReview(reviewDto, userDetails);

		return ResponseEntity.ok().body("리뷰 삭제 성공");

	}

	@GetMapping("/findAll")
	public ResponseEntity<List<ReviewDTO>> findAll(@RequestParam(name = "stationId") String stationId

	) {
		log.info("{}", stationId);
		List<ReviewDTO> review = service.findAll(stationId);
//		
		return ResponseEntity.ok(review);

	}
}
