package com.kh.pcar.back.main.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.pcar.back.main.model.service.MainService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("main")
@RequiredArgsConstructor
public class MainPageController {

	private final MainService ms;

	@GetMapping
	public ResponseEntity<Map<String, Object>> findMainResponse() {

		Map<String, Object> response = ms.findMainResponse();

		return ResponseEntity.ok(response);

	}

}
