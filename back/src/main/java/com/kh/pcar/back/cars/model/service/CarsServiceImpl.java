package com.kh.pcar.back.cars.model.service;

import java.security.InvalidParameterException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kh.pcar.back.auth.model.vo.CustomUserDetails;
import com.kh.pcar.back.cars.model.dao.CarsMapper;
import com.kh.pcar.back.cars.model.dto.CarsDTO;
import com.kh.pcar.back.exception.CarNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CarsServiceImpl implements CarsService {

	private final CarsMapper carsMapper;

	@Override
	public List<CarsDTO> findAll(int pageNo) {

		if (pageNo < 1) {
			throw new InvalidParameterException("잘못된 접근입니다.");
		}

		int limit = 4;
		int offset = (pageNo - 1) * limit;

		List<CarsDTO> cars = carsMapper.findAll(limit, offset);

		checkedCarsEmpty(cars, "조회된 자동차가 없습니다.");

		return cars;
	}

	@Override
	public List<CarsDTO> findByCarId(Long carId) {

		List<CarsDTO> cars = carsMapper.findByCarId(carId);

		checkedCarsEmpty(cars, "차 번호가 조회되지 않습니다.");

		return cars;
	}

	private void checkedCarsEmpty(List<CarsDTO> cars, String msg) {
		if (cars.isEmpty()) {
			throw new CarNotFoundException(msg);
		}
	}
}
