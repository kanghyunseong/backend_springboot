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

		reservationMapper.saveReservation(reservationDTO);

		return reservationDTO.getReservationNo();
	}

	@Override // 확인창
	public List<ReservationDTO> confirmReservation(Long reservationNo) {

		return reservationMapper.confirmReservation(reservationNo);
	}

	@Override // 예약내역창
	public List<CarReservationDTO> findReservation(CustomUserDetails userDetails) {

		Long userNo = userDetails.getUserNo();

		return reservationMapper.findReservation(userNo);
	}

	@Override
	public List<CarReservationDTO> getHistoryReservation(CustomUserDetails userDetails) {

		Long userNo = userDetails.getUserNo();

		return reservationMapper.getHistoryReservation(userNo);
	}

	@Override // 예약 반납
	public int returnReservation(Long resevationNo, CustomUserDetails userDetails) {

		int result = reservationMapper.returnReservation(resevationNo);

		checkUpdateResult(result);

		return result;
	}

	@Override // 예약 변경
	public int changeReservation(ReservationDTO reservation, CustomUserDetails userDetails) {

		int result = reservationMapper.changeReservation(reservation);

		checkUpdateResult(result);

		return result;
	}

	@Override // 예약 취소
	public void cancelReservation(Long reservationNo, CustomUserDetails userDetails) {

		List<ReservationDTO> dto = reservationMapper.confirmReservation(reservationNo);

		if (dto.isEmpty()) {
			throw new ReservationNotFoundException("예약번호를 찾을 수 없습니다.");
		}

		reservationMapper.cancelReservation(reservationNo);
	}

	// 업데이트 문 result 값 확인해서 처리용 메소드
	private void checkUpdateResult(int result) {
		if (result == 0) {
			throw new ReservationNotFoundException("예약번호를 찾을 수 없습니다.");
		}
	}

}
