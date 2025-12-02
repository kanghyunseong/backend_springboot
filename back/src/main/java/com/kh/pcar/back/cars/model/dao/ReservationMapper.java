package com.kh.pcar.back.cars.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kh.pcar.back.cars.model.dto.CarReservationDTO;
import com.kh.pcar.back.cars.model.dto.ReservationDTO;

@Mapper
public interface ReservationMapper {
	Long saveReservation(ReservationDTO reservationDTO);
	
	List<ReservationDTO> confirmReservation(Long reservationNo);
	
	List<CarReservationDTO> findReservation(Long userNo);
	
	List<CarReservationDTO> getHistoryReservation(Long userNo);
	
	int returnReservation(Long resevationNo);
	
	int changeReservation(ReservationDTO reservation);
	
	void cancelReservation(Long reservationNo);
}
