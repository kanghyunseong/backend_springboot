package com.kh.pcar.back.admin.cars.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kh.pcar.back.admin.cars.model.dto.AdminCarDTO;
import com.kh.pcar.back.admin.cars.model.dto.AdminCarPageResponseDTO;
import com.kh.pcar.back.admin.cars.model.dto.AdminCarsReservationDTO;
import com.kh.pcar.back.admin.cars.model.service.AdminCarService;
import com.kh.pcar.back.exception.CarNotFoundException;
import com.kh.pcar.back.exception.ReservationNotFoundException;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/api/settings")
public class AdminCarController {

	private final AdminCarService adminCarService;

	@GetMapping
	public AdminCarPageResponseDTO getAllCar(@RequestParam(name = "page", defaultValue = "1") int page) {
		return adminCarService.findAllCars(page);
	}

	@PostMapping
	public ResponseEntity<String> registerCar(@ModelAttribute AdminCarDTO carDTO,
			@RequestParam(value = "file", required = false) MultipartFile file) {
			adminCarService.registerCar(carDTO, file);
			return ResponseEntity.ok("차량 등록 성공");
	}
	
	

	@PostMapping("/update")
	public ResponseEntity<String> updateCar(@ModelAttribute AdminCarDTO carDTO,
			@RequestParam(value = "file", required = false) MultipartFile file) {
			adminCarService.updateCar(carDTO, file);
			return ResponseEntity.ok("차량 수정 성공");
	}

	@GetMapping("/{carId}")
	public ResponseEntity<Object> getCar(@PathVariable(name = "carId") Long carId) {
		return ResponseEntity.ok(adminCarService.findCarById(carId));
	}

	@DeleteMapping("/{carId}")
	public ResponseEntity<String> deleteCar(@PathVariable(name = "carId") Long carId) {
			adminCarService.deleteCarById(carId);
			return ResponseEntity.ok("차량 삭제에 성공하였습니다.");
	}

	@GetMapping("/reservations")
	public ResponseEntity<List<AdminCarsReservationDTO>> getAllReservation() {

		return ResponseEntity.ok(adminCarService.findAllReservations());
	}

	@GetMapping("/daily-stats")
	public ResponseEntity<List<Map<String, Object>>> getDailyStats() {
		return ResponseEntity.ok(adminCarService.getDailyReservationStats());
	}

	@PutMapping("/reservations/{reservationNo}/cancel")
	public ResponseEntity<String> cancelReservation(@PathVariable("reservationNo") Long reservationNo) {
			adminCarService.cancelReservation(reservationNo);
			return ResponseEntity.ok("예약이 성공적으로 취소되었습니다.");
	}

}
