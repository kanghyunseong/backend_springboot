package com.kh.pcar.back.admin.Enviroments.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.pcar.back.admin.Enviroments.model.dto.AdminEnviromentsDTO;
import com.kh.pcar.back.admin.Enviroments.model.service.AdminEnviromentsService;
import com.kh.pcar.back.common.ResponseData; // ResponseData 임포트 확인

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/ranking")
public class AdminEnviromentsController {

	private final AdminEnviromentsService adminEnviromentsService;

	@GetMapping("/users")
	public ResponseEntity<ResponseData<List<AdminEnviromentsDTO>>> getUserRankings() {
		// 1. 서비스에서 랭킹 데이터 조회
		List<AdminEnviromentsDTO> rankings = adminEnviromentsService.findUserRankings();

		// 2. ResponseData.ok()를 사용하여 200 OK 상태 코드와 함께 반환
		return ResponseData.ok(rankings, "유저 랭킹 목록을 성공적으로 불러왔습니다.");
	}
}