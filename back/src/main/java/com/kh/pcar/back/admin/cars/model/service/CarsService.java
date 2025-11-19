package com.kh.pcar.back.admin.cars.model.service;

import com.kh.pcar.back.admin.cars.model.dto.CarsPageResponseDTO;

public interface CarsService {

	CarsPageResponseDTO findAllCars(int currentPage);
}
