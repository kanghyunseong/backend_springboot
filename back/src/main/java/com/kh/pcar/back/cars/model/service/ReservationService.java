package com.kh.pcar.back.cars.model.service;

import java.util.List;

import com.kh.pcar.back.auth.model.vo.CustomUserDetails;
import com.kh.pcar.back.cars.model.dto.CarReservationDTO;
import com.kh.pcar.back.cars.model.dto.ReservationDTO;

public interface ReservationService {
	Long saveReservation(ReservationDTO reservationDTO, CustomUserDetails userDetails);
	
	List<ReservationDTO> confirmReservation(Long reservationNo);
	
	List<CarReservationDTO> findReservation(CustomUserDetails userDetails);
	
	List<CarReservationDTO> getHistoryReservation(CustomUserDetails userDetails);
	
	void returnReservation(Long resevationNo, CustomUserDetails userDetails);
	
	void changeReservation(ReservationDTO reservation, CustomUserDetails userDetails);
	
	void cancelReservation(Long reservationNo, CustomUserDetails userDetails);
}
