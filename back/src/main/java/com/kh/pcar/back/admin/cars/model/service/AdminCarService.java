package com.kh.pcar.back.admin.cars.model.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.kh.pcar.back.admin.cars.model.dto.AdminCarDTO;
import com.kh.pcar.back.admin.cars.model.dto.AdminCarPageResponseDTO;
import com.kh.pcar.back.admin.cars.model.dto.AdminCarsReservationDTO;

public interface AdminCarService {

	AdminCarPageResponseDTO findAllCars(int currentPage);

	void registerCar(AdminCarDTO carDTO, MultipartFile file);

	void updateCar(AdminCarDTO carDTO, MultipartFile file);

	AdminCarDTO findCarById(Long carId);

	void deleteCarById(Long carId);

	List<AdminCarsReservationDTO> findAllReservations();
	
	List<Map<String, Object>> getDailyReservationStats();

	void cancelReservation(Long reservationNo);
	
	
}
