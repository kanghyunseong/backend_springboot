package com.kh.pcar.back.cars.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.pcar.back.auth.model.vo.CustomUserDetails;
import com.kh.pcar.back.cars.model.dto.CarsDTO;
import com.kh.pcar.back.cars.model.dto.ReservationDTO;
import com.kh.pcar.back.cars.model.service.CarsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/cars")
public class CarsController {
	
	private final CarsService carsService;
	
	// 차량 전체 조회
	@GetMapping
	public ResponseEntity<List<CarsDTO>> findAll(@RequestParam(value = "page", defaultValue = "0")int pageNo) {
		
		List<CarsDTO> cars = carsService.findAll(pageNo);
		
		return ResponseEntity.ok(cars);
	}
	
	// 차량 상세 조회
	// GET /
	@GetMapping("/{carId}")
	public ResponseEntity<List<CarsDTO>> findByCarId(@PathVariable(name="carId") Long carId) {
		
		List<CarsDTO> car = carsService.findByCarId(carId);
		
		return ResponseEntity.ok(car);
	}

	@PostMapping("/reserve")
	public ResponseEntity<Long> saveReservation(
	        @RequestBody ReservationDTO reservationDTO,
	        @AuthenticationPrincipal CustomUserDetails userDetails) {
	    
	    Long reservationNo = carsService.saveReservation(reservationDTO, userDetails);
	    
	    return ResponseEntity.ok(reservationNo);
	}
	
	@GetMapping("/reserve/{reservationNo}/confirm")
	public ResponseEntity<List<ReservationDTO>> confirmReservation(@PathVariable(name="reservationNo")Long reservationNo) {
		
		List<ReservationDTO> reservation = carsService.confirmReservation(reservationNo);
		
		return ResponseEntity.ok(reservation);
	}
	
	
}
