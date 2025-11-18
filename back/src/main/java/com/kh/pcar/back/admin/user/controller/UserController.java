package com.kh.pcar.back.admin.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.pcar.back.admin.user.model.dto.UserPageResponseDTO;
import com.kh.pcar.back.admin.user.model.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/api/users")
public class UserController {
	
	private final UserService userService;
	
	/**
	 * [GET] /api/admin/users?page=1
	 */
	@GetMapping
	public UserPageResponseDTO getAllUsers(
            @RequestParam(name="page", defaultValue="1") int page
    ) {
		// Service에 현재 페이지 번호를 전달
		return userService.findAllMember(page);
	}
}