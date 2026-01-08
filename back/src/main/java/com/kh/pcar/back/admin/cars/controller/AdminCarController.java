package com.kh.pcar.back.admin.cars.controller;

import java.util.List;
import java.util.Map;

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
import com.kh.pcar.back.common.ResponseData;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/settings")
public class AdminCarController {

   private final AdminCarService adminCarService;
   

   // 1. 목록 조회 (200 OK)
   @GetMapping
   public ResponseEntity<ResponseData<AdminCarPageResponseDTO>> getAllCar(
         @RequestParam(name = "page", defaultValue = "1") int page) {
      AdminCarPageResponseDTO list = adminCarService.findAllCars(page);
      return ResponseData.ok(list);
   }

   // 2. 차량 등록 (201 Created)
   @PostMapping
   public ResponseEntity<ResponseData<String>> registerCar(@ModelAttribute AdminCarDTO carDTO,
         @RequestParam(value = "file", required = false) MultipartFile file) {
      adminCarService.registerCar(carDTO, file);
      return ResponseData.created("차량 등록이 성공적으로 완료되었습니다.");
   }

   // 3. 차량 수정 (200 OK + 메시지)
   @PutMapping("/update")
   public ResponseEntity<ResponseData<String>> updateCar(@ModelAttribute AdminCarDTO carDTO,
         @RequestParam(value = "file", required = false) MultipartFile file) {
      adminCarService.updateCar(carDTO, file);
      // 수정 완료 후 메시지를 보여줘야 하므로 ok 사용
      return ResponseData.ok(null, "차량 정보가 수정되었습니다.");
   }

   // 4. 단일 조회 (200 OK)
   @GetMapping("/{carId}")
   public ResponseEntity<ResponseData<AdminCarDTO>> getCar(@PathVariable("carId") Long carId) {
      AdminCarDTO car = adminCarService.findCarById(carId);
      return ResponseData.ok(car);
   }

   // 5. 차량 삭제 (204 No Content) - 중요: 바디를 보내지 않음
   @DeleteMapping("/{carId}")
   public ResponseEntity<ResponseData<Void>> deleteCar(@PathVariable("carId") Long carId) {
      adminCarService.deleteCarById(carId);
      return ResponseData.noContent();
   }

   // 6. 예약 목록 조회 (200 OK)
   @GetMapping("/reservations")
   public ResponseEntity<ResponseData<List<AdminCarsReservationDTO>>> getAllReservation() {
      List<AdminCarsReservationDTO> list = adminCarService.findAllReservations();
      return ResponseData.ok(list);
   }

   // 7. 통계 데이터 조회 (200 OK)
   @GetMapping("/daily-stats")
   public ResponseEntity<ResponseData<List<Map<String, Object>>>> getDailyStats() {
      List<Map<String, Object>> stats = adminCarService.getDailyReservationStats();
      return ResponseData.ok(stats);
   }

   // 8. 예약 취소 (200 OK + 메시지)
   @PutMapping("/reservations/{reservationNo}/cancel")
   public ResponseEntity<ResponseData<String>> cancelReservation(@PathVariable("reservationNo") Long reservationNo) {
      adminCarService.cancelReservation(reservationNo);
      return ResponseData.ok(null, "예약이 성공적으로 취소되었습니다.");
   }
}