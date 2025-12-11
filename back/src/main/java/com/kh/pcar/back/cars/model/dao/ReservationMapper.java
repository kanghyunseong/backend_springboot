package com.kh.pcar.back.cars.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.kh.pcar.back.cars.model.dto.CarReservationDTO;
import com.kh.pcar.back.cars.model.dto.ReservationDTO;

@Mapper
public interface ReservationMapper {
	Long saveReservation(ReservationDTO reservationDTO);
	
	List<ReservationDTO> confirmReservation(Long reservationNo);
	
	List<CarReservationDTO> findReservation(Long userNo);
	
	List<CarReservationDTO> getHistoryReservation(Long userNo);
	
	@Select("""
		SELECT
		       COUNT(*)
		  FROM
		       TB_RESERVATION
		 WHERE
		       RESERVATION_STATUS = 'Y'
		   AND
		       (
		           USER_NO = #{userNo}
		        OR
		           CAR_ID = #{carId}
		       ) 
			""")
	int countByReservation(ReservationDTO reservationDTO); // 유저가 예약한 내역이 있으면 찾아주는 메서드
	
	int returnReservation(Long resevationNo);
	
	int changeReservation(ReservationDTO reservation);
	
	void cancelReservation(Long reservationNo);
}
