package com.kh.pcar.back.admin.cars.model.service;

import com.kh.pcar.back.admin.cars.model.dto.AdminCarPageResponseDTO;

public interface AdminCarService {

	AdminCarPageResponseDTO findAllCars(int currentPage);
}
