package com.kh.pcar.back.cars.model.service;

import java.util.List;

import com.kh.pcar.back.auth.model.vo.CustomUserDetails;
import com.kh.pcar.back.cars.model.dto.CarsDTO;

public interface CarsService {
	
	List<CarsDTO> findAll(int pageNo);
	
	List<CarsDTO> findByCarId(Long carId);
	
}
