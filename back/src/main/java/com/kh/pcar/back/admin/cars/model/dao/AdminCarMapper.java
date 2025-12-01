package com.kh.pcar.back.admin.cars.model.dao;

import java.util.List;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.kh.pcar.back.admin.cars.model.dto.AdminCarDTO;
import com.kh.pcar.back.admin.cars.model.dto.AdminCarsReservationDTO;

@Mapper
public interface AdminCarMapper {

	// 전체 목록을 페이징 처리
	List<AdminCarDTO> findAllCars(RowBounds rowBounds);
	
	int getTotalCount();

	void insertCar(AdminCarDTO carDTO);

	void updateCar(AdminCarDTO carDTO);

	int updateCarStatus(Long carId);
	
	List<AdminCarsReservationDTO> findAllReservations();
	
	List<Map<String, Object>> getWeeklyCarbonSavings();
	
	List<Map<String, Object>> getDailyReservationStats();

	int updateReservationStatus(@Param("reservationNo") Long reservationNo, @Param("status") String status);
	
	List<Map<String, Object>> getTotalCarbonSavingsByUser();
	
	Map<String, Object> getReservationSavingDataByNo(Long reservationNo);
	
	void insertCarbonSaving(Map<String, Object> savingData);

	AdminCarDTO findCarById(Long carId);
	
	
}
  