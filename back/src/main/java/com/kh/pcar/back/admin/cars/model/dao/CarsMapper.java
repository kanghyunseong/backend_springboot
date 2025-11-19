package com.kh.pcar.back.admin.cars.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.kh.pcar.back.admin.cars.model.dto.CarsDTO;

@Mapper
public interface CarsMapper {

	// 전체 목록을 페이징 처리
	List<CarsDTO> findAllCars(RowBounds rowBounds);
	
	 int getTotalCount();
	
}
