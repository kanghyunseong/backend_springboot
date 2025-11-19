package com.kh.pcar.back.cars.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kh.pcar.back.cars.model.dto.CarsDTO;

@Mapper
public interface CarsMapper {
	
	int selectTotalCount();

	List<CarsDTO> findAll(@Param("limit") int limit, @Param("offset") int offset);
	
	List<CarsDTO> findByCarId(long carId);
	
}
