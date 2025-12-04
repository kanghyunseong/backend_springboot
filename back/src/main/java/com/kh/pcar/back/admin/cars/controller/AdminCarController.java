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
    public ResponseEntity<AdminCarPageResponseDTO> getAllCar(
            @RequestParam(name = "page", defaultValue = "1") int page) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(adminCarService.findAllCars(page));
    }


	@PostMapping
    public ResponseEntity<String> registerCar(
            @ModelAttribute AdminCarDTO carDTO,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        adminCarService.registerCar(carDTO, file);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("차량 등록이 완료되었습니다.");
    }
	
	

	@PutMapping("/update")
    public ResponseEntity<String> updateCar(
            @ModelAttribute AdminCarDTO carDTO,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        adminCarService.updateCar(carDTO, file);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("차량 정보가 수정되었습니다.");
    }

	@GetMapping("/{carId}")
    public ResponseEntity<AdminCarDTO> getCar(@PathVariable Long carId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(adminCarService.findCarById(carId));
    }

	@DeleteMapping("/{carId}")
    public ResponseEntity<String> deleteCar(@PathVariable Long carId) {

        adminCarService.deleteCarById(carId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("차량 삭제가 완료되었습니다.");
    }

	@GetMapping("/reservations")
    public ResponseEntity<List<AdminCarsReservationDTO>> getAllReservation() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(adminCarService.findAllReservations());
    }

	@GetMapping("/daily-stats")
    public ResponseEntity<List<Map<String, Object>>> getDailyStats() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(adminCarService.getDailyReservationStats());
    }

	@PutMapping("/reservations/{reservationNo}/cancel")
    public ResponseEntity<String> cancelReservation(
            @PathVariable Long reservationNo) {

        adminCarService.cancelReservation(reservationNo);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("예약이 성공적으로 취소되었습니다.");
    }

}
