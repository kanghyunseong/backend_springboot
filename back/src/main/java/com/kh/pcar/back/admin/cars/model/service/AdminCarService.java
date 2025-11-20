package com.kh.pcar.back.admin.cars.model.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.kh.pcar.back.admin.cars.model.dto.AdminCarDTO;
import com.kh.pcar.back.admin.cars.model.dto.AdminCarPageResponseDTO;

public interface AdminCarService {

	AdminCarPageResponseDTO findAllCars(int currentPage);

	void registerCar(AdminCarDTO carDTO, MultipartFile file) throws IOException;

	void updateCar(AdminCarDTO carDTO, MultipartFile file) throws IOException;

	Object findCarById(Long carId);

	void deleteCarById(Long carId);
	
	
}
