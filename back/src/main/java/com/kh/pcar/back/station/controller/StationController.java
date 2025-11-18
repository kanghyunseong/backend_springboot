package com.kh.pcar.back.station.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kh.pcar.back.station.model.service.ServiceStation;
import com.kh.pcar.back.station.model.service.ServiceStationImpl;

//import com.kh.pcar.back.station.model.service.ServiceStation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/station")
@RequiredArgsConstructor
public class StationController {	
	private final ServiceStation service;
	@GetMapping("/EvCharge")
	public String station(@RequestParam(name= "lat")String lat,@RequestParam(name="lng")String lng) {
//		log.info("{}",page);
//		log.info("{}",serviceKey);
//		log.info("{}",perPage);
		log.info("{},{}",lat,lng);
		String response = service.station(lat,lng);
		return"";
	}

}
