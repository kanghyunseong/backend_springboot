package com.kh.pcar.back.cars.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kh.pcar.back.cars.model.dto.CarReservationDTO;
import com.kh.pcar.back.cars.model.dto.CarsDTO;
import com.kh.pcar.back.cars.model.dto.ReservationDTO;

@Mapper
public interface CarsMapper {
	
	int selectTotalCount();

	List<CarsDTO> findAll(@Param("limit") int limit, @Param("offset") int offset);
	
	List<CarsDTO> findByCarId(Long carId);
	
}
