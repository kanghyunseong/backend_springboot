package com.kh.pcar.back.admin.user.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.kh.pcar.back.common.ResponseData;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/users") // React 요청 주소와 일치
public class UserController {

    private final UserService userService;

    // 1. 회원 전체 목록 조회 (페이징)
    @GetMapping
    public ResponseEntity<ResponseData<UserPageResponseDTO>> getAllUsers(@RequestParam(name = "page", defaultValue = "1") int page) {
        UserPageResponseDTO list = userService.findAllMember(page);
        return ResponseData.ok(list, "회원 목록 조회 성공");
    }

    // 2. 회원 삭제 (204 No Content 적용)
    @DeleteMapping("/{userNo}")
    public ResponseEntity<ResponseData<Void>> deleteUser(
            @PathVariable(name = "userNo") Long userNo, 
            @AuthenticationPrincipal CustomUserDetails adminUser) {
        
        userService.deleteUser(userNo);
        // 삭제 성공 시 본문 없이 204 상태 코드만 반환
        return ResponseData.noContent();
    }
    
    // 3. 회원 정보 수정 (200 OK + 수정된 데이터)
    @PutMapping
    public ResponseEntity<ResponseData<UserDTO>> updateUser(@RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(userDTO);
        return ResponseData.ok(updatedUser, "회원 정보가 수정되었습니다.");
    }
    
    // 4. 특정 회원 상세 조회
    @GetMapping("/{userNo}")
    public ResponseEntity<ResponseData<UserDTO>> getUser(@PathVariable(name="userNo") Long userNo) {
        UserDTO user = userService.findUserByNo(userNo);
        return ResponseData.ok(user, "회원 상세 조회 성공");
    }
    
    // 5. 가입 추이 통계 조회
    @GetMapping("/trend")
    public ResponseEntity<ResponseData<List<Map<String, Object>>>> getJoinTrend(
            @RequestParam(name = "unit", defaultValue = "month") String unit) {
        List<Map<String, Object>> trend = userService.getJoinTrend(unit);
        return ResponseData.ok(trend, "가입 추이 데이터 조회 성공");
    }
    
    // 6. KPI 통계 데이터 조회
    @GetMapping("/kpi")
    public ResponseEntity<ResponseData<UserKpiStatsDTO>> getKpiStats() {
        UserKpiStatsDTO stats = userService.getKpiStats();
        return ResponseData.ok(stats, "KPI 통계 조회 성공");
    }
    
    // 7. 면허 상태 추이 통계 조회
    @GetMapping("/license/trend")
    public ResponseEntity<ResponseData<List<Map<String, Object>>>> getLicenseStatusTrend(
            @RequestParam(name = "unit", defaultValue = "month") String unit) {
        List<Map<String, Object>> trendData = userService.getLicenseStatusTrend(unit);
        return ResponseData.ok(trendData, "면허 상태 추이 데이터 조회 성공");
    }
}