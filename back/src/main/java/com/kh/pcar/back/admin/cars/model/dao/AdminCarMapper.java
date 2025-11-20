package com.kh.pcar.back.admin.cars.model.dao;

import java.util.List;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.kh.pcar.back.admin.cars.model.dto.AdminCarDTO;

@Mapper
public interface AdminCarMapper {

	// 전체 목록을 페이징 처리
	List<AdminCarDTO> findAllCars(RowBounds rowBounds);
	
	 int getTotalCount();

	void insertCar(AdminCarDTO carDTO);

	void updateCar(AdminCarDTO carDTO);

	int updateCarStatus(Long carId);
	
}
