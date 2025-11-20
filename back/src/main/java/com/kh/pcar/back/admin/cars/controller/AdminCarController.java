package com.kh.pcar.back.admin.cars.controller;

import org.springframework.web.bind.annotation.GetMapping;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.pcar.back.admin.cars.model.dto.AdminCarPageResponseDTO;
import com.kh.pcar.back.admin.cars.model.service.AdminCarService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/api/settings")
public class AdminCarController {

	private final AdminCarService adminCarService;
	
	@GetMapping
	public AdminCarPageResponseDTO getAllCar(@RequestParam(name = "page", defaultValue = "1") int page) {
		return adminCarService.findAllCars(page);
	}
	
	
	
}
