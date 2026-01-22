package com.kh.pcar.back.cars.model.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kh.pcar.back.auth.model.vo.CustomUserDetails;
import com.kh.pcar.back.cars.model.dao.ReservationMapper;
import com.kh.pcar.back.cars.model.dto.CarReservationDTO;
import com.kh.pcar.back.cars.model.dto.ReservationDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class ReservationServiceImplTest {

	@Mock
	private ReservationMapper reservationMapper;

	@InjectMocks
	private ReservationServiceImpl reservationService;

	// 공통으로 사용할 변수들
	private Long reservationNo;
	private Long userNo;
	private CustomUserDetails mockUser;

	@BeforeEach
	public void setUp() {
		reservationNo = 20L;
		userNo = 1L;
		mockUser = mock(CustomUserDetails.class);
	}

	@Test
	void 내역확인창() {
		// given
		when(mockUser.getUserNo()).thenReturn(userNo);
		ReservationDTO mockDto = new ReservationDTO();
		mockDto.setUserNo(userNo);
		mockDto.setReservationNo(reservationNo);

		CarReservationDTO carReservationDTO = new CarReservationDTO();
		carReservationDTO.setReservation(mockDto);

		List<CarReservationDTO> listDto = new ArrayList<>();
		listDto.add(carReservationDTO);

		// when
		when(reservationMapper.findReservation(userNo))
			.thenReturn(listDto);

		List<CarReservationDTO> result = reservationService.findReservation(mockUser);

		// then
		assertThat(result.get(0).getReservation().getReservationNo()).isEqualTo(reservationNo);
	}

	@Test
	void 예약반납() {
		// given
		when(reservationMapper.returnReservation(reservationNo))
			.thenReturn(1);

		// when
		reservationService.returnReservation(reservationNo, mockUser);

		// then
		verify(reservationMapper, times(1)).returnReservation(reservationNo);
	}

	@Test
	void 예약변경() {
		ReservationDTO reservation = new ReservationDTO();
		
		reservation.setReservationNo(reservationNo);
		reservation.setUserNo(userNo);
		when(reservationMapper.changeReservation(reservation))
			.thenReturn(1);
	
		reservationService.changeReservation(reservation, mockUser);
		
		verify(reservationMapper, times(1)).changeReservation(reservation);
	}
	
	@Test
	void 예약취소() {
		
		
		
		
		
	}
}