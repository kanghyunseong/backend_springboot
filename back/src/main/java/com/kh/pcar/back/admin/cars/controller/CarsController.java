package com.kh.pcar.back.admin.cars.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.pcar.back.admin.cars.model.dto.CarsPageResponseDTO;
import com.kh.pcar.back.admin.cars.model.service.CarsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/api/settings")
public class CarsController {

	private final CarsService carsService;
	
	@GetMapping
	public CarsPageResponseDTO getAllCars(@RequestParam(name = "page", defaultValue = "1") int page) {
		return carsService.findAllCars(page);
	}
	
	
	
}
