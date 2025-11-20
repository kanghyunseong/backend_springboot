package com.kh.pcar.back.cars.model.service;

import java.security.InvalidParameterException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kh.pcar.back.auth.model.vo.CustomUserDetails;
import com.kh.pcar.back.cars.model.dao.CarsMapper;
import com.kh.pcar.back.cars.model.dto.CarsDTO;
import com.kh.pcar.back.cars.model.dto.ReservationDTO;
import com.kh.pcar.back.exception.CarAuthorizationException;
import com.kh.pcar.back.exception.CustomAuthenticationException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CarsServiceImpl implements CarsService {

	private final CarsMapper carsMapper;
	
	@Override
	public List<CarsDTO> findAll(int pageNo) {
		
		if(pageNo < 1) {
			throw new InvalidParameterException("잘못된 접근입니다.");
		}
		
		int limit = 4;
		int offset = (pageNo - 1) * limit;
		
		return carsMapper.findAll(limit, offset);
	}
	
	@Override
	public List<CarsDTO> findByCarId(Long carId) {
		
		return carsMapper.findByCarId(carId);
	}
	
	@Override
	public void saveReservation(ReservationDTO reservationDTO, CustomUserDetails userDetails) {
		
	    reservationDTO.setUserNo(userDetails.getUserNo());
	    
	    reservationDTO.setReservationStatus("R");
		
	    carsMapper.saveReservation(reservationDTO);
	}
}
