package com.kh.pcar.back.main.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.pcar.back.main.model.dao.MainMapper;
import com.kh.pcar.back.main.model.dto.PopularCarDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MainServiceImpl implements MainService {

	private final MainMapper mapper;

	@Override
	@Transactional
	public Map<String, Object> findMainResponse() {

		String countCars = mapper.countCar();

		String countReservations = mapper.countReservation();

		String countMembers = mapper.countMember();
		
		String countRentalCars = mapper.countRentalCars();

		List<PopularCarDTO> popularCars = mapper.findPopularCar();

		//log.info("car : {} , Reservation , {} , countMembers : {}  , popularcars : {} ", countCars, countReservations,
		//countMembers, popularCars);

		Map<String, Object> response = Map.of("countCars", countCars, "countReservation", countReservations,
				"countMembers", countMembers, "countRentalCars", countRentalCars, "popularCars", popularCars);

		// log.info("response : {} ", response);

		return response;
	}

}
