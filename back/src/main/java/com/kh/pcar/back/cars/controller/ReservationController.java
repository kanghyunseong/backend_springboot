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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/reserve")
public class ReservationController {
	private final ReservationService reservationService;
	
	// 예약 (INSERT)
	@PostMapping
	public ResponseEntity<Long> saveReservation(
	        @RequestBody ReservationDTO reservationDTO,
	        @AuthenticationPrincipal CustomUserDetails userDetails) {
	    
	    Long reservationNo = reservationService.saveReservation(reservationDTO, userDetails);
	    
	    return ResponseEntity.ok(reservationNo); //reservationNo를 url로 뽑아쓰기위해 반환
	}
	
	// 예약 확인창
	@GetMapping("/{reservationNo}")
	public ResponseEntity<List<ReservationDTO>> confirmReservation(@PathVariable(name="reservationNo")Long reservationNo) {
		
		List<ReservationDTO> reservation = reservationService.confirmReservation(reservationNo);
		
		return ResponseEntity.ok(reservation); // 예약확인창에서 예약정보를 띄워주기
	}
	
	
	// 예약 내역창
	@GetMapping("/searchList")
	public ResponseEntity<List<CarReservationDTO>> findReservation(@AuthenticationPrincipal CustomUserDetails userDetails) {
		
		List<CarReservationDTO> reservation = reservationService.findReservation(userDetails);
		
		return ResponseEntity.ok(reservation); // 사용자가 예약한 예약정보 
	}
	
	// 예약 반납
	@PutMapping("/return")
	public ResponseEntity<?> returnReservation(@RequestBody Long reservationNo, 
											  @AuthenticationPrincipal CustomUserDetails userDetails){

		int result = reservationService.returnReservation(reservationNo, userDetails);
		
		if(result > 0) { // 200 ok
			return ResponseEntity.ok("반납 완료 하였습니다.");
		} else { // 400 badRequest
			return ResponseEntity.badRequest().body("반납 처리 실패하였습니다.");
		}
	}
	
	// 예약 변경 
	@PutMapping("/change")
	public ResponseEntity<?> changeReservation(@RequestBody ReservationDTO reservation,
											   @AuthenticationPrincipal CustomUserDetails userDetails) {
		
		reservationService.changeReservation(reservation, userDetails);
		
		return ResponseEntity.ok().body("예약 변경 완료");
	}
	
	// 예약 취소
	@DeleteMapping("/{reservationNo}")
	public ResponseEntity<String> cancelReservation(@PathVariable(name="reservationNo") Long reservationNo, 
											   @AuthenticationPrincipal CustomUserDetails userDetails) {
		
		reservationService.cancelReservation(reservationNo, userDetails);
		
		return ResponseEntity.ok().body("예약 취소 완료");
	}
	
	
	
}
