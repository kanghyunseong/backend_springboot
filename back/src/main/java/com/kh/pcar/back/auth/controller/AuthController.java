
package com.kh.pcar.back.auth.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.pcar.back.auth.model.dto.MemberLoginDTO;
import com.kh.pcar.back.auth.model.service.AuthService;
import com.kh.pcar.back.auth.model.service.SocialAuthService;
import com.kh.pcar.back.common.ResponseData;
import com.kh.pcar.back.token.model.service.TokenService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

	private final AuthService authService;
	private final TokenService tokenService;
	private final SocialAuthService socialAuthService;

	@PostMapping("/login")
	public ResponseEntity<ResponseData<Object>> login(@Valid @RequestBody MemberLoginDTO member) {
		Map<String, String> loginResponse = authService.login(member);
		return ResponseData.ok(loginResponse,"로그인 성공");
	}

	@GetMapping("/{provider}/callback")
	public ResponseEntity<ResponseData<Object>> callBack(@PathVariable("provider") String provider,
			@RequestParam("code") String code, @RequestParam(value = "state", required = false) String state) {

		// log.info("콜백 code={}, state={}", code, state);

	

		// log.info("response : {} " , response );
		return ResponseData.ok(socialAuthService.processCallback(provider, code, state));

	} // 홈으로 이동

	@PostMapping("/refresh")
	public ResponseEntity<ResponseData<Map<String, String>>> refresh(@RequestBody Map<String, String> token) {

		String refreshToken = token.get("refreshToken");
	

		return ResponseData.created(tokenService.validateToken(refreshToken));

	}

}
