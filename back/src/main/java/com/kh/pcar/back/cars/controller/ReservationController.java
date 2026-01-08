package com.kh.pcar.back.cars.controller;

import java.util.List;

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
import com.kh.pcar.back.cars.model.dto.CarReservationDTO;
import com.kh.pcar.back.cars.model.dto.ReservationDTO;
import com.kh.pcar.back.cars.model.service.ReservationService;
import com.kh.pcar.back.common.ResponseData;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/reserve")
public class ReservationController {
	private final ReservationService reservationService;

	// 예약 (INSERT)
	@PostMapping
	public ResponseEntity<ResponseData<Long>> saveReservation(@RequestBody ReservationDTO reservationDTO,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		
		return ResponseData.ok(reservationService.saveReservation(reservationDTO, userDetails),"예약이 완료되었습니다."); // reservationNo를 url로 뽑아쓰기위해 반환
	}

	// 예약 확인창
	@GetMapping("/{reservationNo:[0-9]+}")
	public ResponseEntity<ResponseData<List<ReservationDTO>>> confirmReservation(
			@PathVariable(name = "reservationNo") Long reservationNo) {

		return ResponseData.ok(reservationService.confirmReservation(reservationNo), "조회성공"); // 예약확인창에서 예약정보를 띄워주기
	}

	// 예약 내역창
	@GetMapping("/searchList")
	public ResponseEntity<ResponseData<List<CarReservationDTO>>> findReservation(
			@AuthenticationPrincipal CustomUserDetails userDetails) {

		return ResponseData.ok(reservationService.findReservation(userDetails), "조회성공"); // 사용자가 예약한 예약정보
	}

	// 예약 히스토리창
	@GetMapping("/history")
	public ResponseEntity<ResponseData<List<CarReservationDTO>>> getHistoryReservation(
			@AuthenticationPrincipal CustomUserDetails userDetails) {

		return ResponseData.ok(reservationService.getHistoryReservation(userDetails), "조회성공"); // 사용자가 예약한 예약정보
	}

	// 예약 반납
	@PutMapping("/return")
	public ResponseEntity<ResponseData<String>> returnReservation(@RequestBody Long reservationNo,
			@AuthenticationPrincipal CustomUserDetails userDetails) {

		reservationService.returnReservation(reservationNo, userDetails);

		return ResponseData.ok("반납 완료 하였습니다.", null);
	}

	// 예약 변경
	@PutMapping("/change")
	public ResponseEntity<ResponseData<String>> changeReservation(@RequestBody ReservationDTO reservation,
			@AuthenticationPrincipal CustomUserDetails userDetails) {

		reservationService.changeReservation(reservation, userDetails);

		return ResponseData.ok("예약 변경 완료", null);
	}

	// 예약 취소
	@DeleteMapping("/{reservationNo:[0-9]+}")
	public ResponseEntity<ResponseData<String>> cancelReservation(@PathVariable(name = "reservationNo") Long reservationNo,
			@AuthenticationPrincipal CustomUserDetails userDetails) {

		reservationService.cancelReservation(reservationNo, userDetails);

		return ResponseData.ok("예약 취소 완료", null);
	}

}
