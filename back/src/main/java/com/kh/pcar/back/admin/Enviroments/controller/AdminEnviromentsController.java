package com.kh.pcar.back.admin.Enviroments.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.pcar.back.admin.Enviroments.model.dto.AdminEnviromentsDTO;
import com.kh.pcar.back.admin.Enviroments.model.service.AdminEnviromentsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/api/ranking")
public class AdminEnviromentsController {
	
	private final AdminEnviromentsService adminEnviromentsService;
	
	@GetMapping("/users") 
    public ResponseEntity<List<AdminEnviromentsDTO>> getUserRankings() {
        List<AdminEnviromentsDTO> rankings = adminEnviromentsService.findUserRankings();
        return ResponseEntity.ok(rankings);
    }

}
