package com.kh.pcar.back.cars.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.kh.pcar.back.cars.model.dto.CarsDTO;

@Mapper
public interface CarsMapper {

	List<CarsDTO> findAll(RowBounds rb);
	
}
