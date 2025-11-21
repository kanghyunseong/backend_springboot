package com.kh.pcar.back.admin.user.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity; // import 추가
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping; // import 추가
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable; // import 추가
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.pcar.back.admin.user.model.dto.UserDTO;
import com.kh.pcar.back.admin.user.model.dto.UserKpiStatsDTO;
import com.kh.pcar.back.admin.user.model.dto.UserPageResponseDTO;
import com.kh.pcar.back.admin.user.model.service.UserService;
import com.kh.pcar.back.auth.model.vo.CustomUserDetails;

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
	public ResponseEntity<String> deleteUser(@PathVariable(name = "userNo") Long userNo, @AuthenticationPrincipal CustomUserDetails adminUser) {
		
		if(adminUser != null && adminUser.getUserNo().equals(userNo)) {
	        return ResponseEntity.status(HttpStatus.CONFLICT)
	                             .body("자기 자신의 계정은 삭제할 수 없습니다.");
	    }
		
		userService.deleteUser(userNo);
		return ResponseEntity.ok("삭제 성공");
	}
	
	@PutMapping
	public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO) {
		UserDTO updatedUser = userService.updateUser(userDTO);
		return ResponseEntity.ok(updatedUser);
	}
	
	@GetMapping("/{userNo}")
	public ResponseEntity<UserDTO> getUser(@PathVariable(name="userNo") Long userNo) {
		UserDTO user = userService.findUserByNo(userNo);
		return ResponseEntity.ok(user);
	}
	@GetMapping("/trend")
	public ResponseEntity<List<Map<String, Object>>> getJoinTrend(@RequestParam(name = "unit", defaultValue = "month") String unit) {
	    return ResponseEntity.ok(userService.getJoinTrend(unit));
	}
	
	@GetMapping("/kpi")
	public ResponseEntity<UserKpiStatsDTO> getKpiStats() {
		UserKpiStatsDTO stats = userService.getKpiStats();
		return ResponseEntity.ok(stats);
	}
	
	@GetMapping("/license/trend")
	public ResponseEntity<List<Map<String, Object>>> getLicenseStatusTrend(@RequestParam(name =  "unit", defaultValue = "month") String unit) {
	
		List<Map<String, Object>> trendData = userService.getLicenseStatusTrend(unit);
		return ResponseEntity.ok(trendData);
	}
	
	
}