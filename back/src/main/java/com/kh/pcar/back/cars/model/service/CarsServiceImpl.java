package com.kh.pcar.back.cars.model.service;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import com.kh.pcar.back.cars.model.dao.CarsMapper;
import com.kh.pcar.back.cars.model.dto.CarsDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CarsServiceImpl implements CarsService {

	private final CarsMapper carsMapper;
	
	@Override
	public List<CarsDTO> findAll(int pageNo) {
		
		RowBounds rb = new RowBounds((pageNo - 1) * 4, 4);
		
		return carsMapper.findAll(rb);
	}
}
