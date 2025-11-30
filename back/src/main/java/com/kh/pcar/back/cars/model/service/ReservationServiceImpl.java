package com.kh.pcar.back.cars.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kh.pcar.back.auth.model.vo.CustomUserDetails;
import com.kh.pcar.back.cars.model.dao.ReservationMapper;
import com.kh.pcar.back.cars.model.dto.CarReservationDTO;
import com.kh.pcar.back.cars.model.dto.ReservationDTO;
import com.kh.pcar.back.exception.ReservationNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {
	private final ReservationMapper reservationMapper;
	
	@Override
	public Long saveReservation(ReservationDTO reservationDTO, CustomUserDetails userDetails) {
		
	    reservationDTO.setUserNo(userDetails.getUserNo());
	    
	    reservationDTO.setReservationStatus("Y");
		
	    reservationMapper.saveReservation(reservationDTO);
	    
	    return reservationDTO.getReservationNo();
	}
	
	@Override // 확인창
	public List<ReservationDTO> confirmReservation(Long reservationNo) {
		
		return reservationMapper.confirmReservation(reservationNo);
	}
	
	@Override //예약내역창
	public List<CarReservationDTO> findReservation(CustomUserDetails userDetails) {
		
		Long userNo = userDetails.getUserNo();
		
		return reservationMapper.findReservation(userNo);
	}
	
	@Override // 예약반납
	public int returnReservation(Long resevationNo, CustomUserDetails userDetails) {
		
//	    int result = reservationMapper.returnReservation(resevationNo);
//	    
//	    if(result == 0) {
//	        throw new ReservationNotFoundException("예약번호를 찾을 수 없습니다.");
//	    }
//	    
//		return result;
		
		return reservationMapper.returnReservation(resevationNo);
	}

	@Override  // 예약 변경
	public int changeReservation(ReservationDTO reservation, CustomUserDetails userDetails) {
		
		return reservationMapper.changeReservation(reservation);
	}

	@Override  // 예약 취소
	public void cancelReservation(Long reservationNo, CustomUserDetails userDetails) {
		
		reservationMapper.cancelReservation(reservationNo);
	}
	
}
