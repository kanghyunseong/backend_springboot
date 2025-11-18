package com.kh.pcar.back.admin.user.controller;

import org.springframework.http.ResponseEntity; // import 추가
import org.springframework.web.bind.annotation.DeleteMapping; // import 추가
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable; // import 추가
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.pcar.back.admin.user.model.dto.UserPageResponseDTO;
import com.kh.pcar.back.admin.user.model.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/api/users") // React 요청 주소와 일치
public class UserController {

	private final UserService userService;

	
	@GetMapping
	public UserPageResponseDTO getAllUsers(@RequestParam(name = "page", defaultValue = "1") int page) {
		return userService.findAllMember(page);
	}

	
	@DeleteMapping("/{userNo}")
	public ResponseEntity<String> deleteUser(@PathVariable(name = "userNo") Long userNo) {
		userService.deleteUser(userNo);
		return ResponseEntity.ok("삭제 성공");
	}
}