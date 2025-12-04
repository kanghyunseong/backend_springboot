
package com.kh.pcar.back.auth.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
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
import com.kh.pcar.back.token.model.service.TokenService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("members")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

	private final AuthService authService;
	private final TokenService tokenService;
	private final SocialAuthService socialAuthService;

	@PostMapping("/login")
	public ResponseEntity<Map<String, String>> login(@Valid @RequestBody MemberLoginDTO member) {
		Map<String, String> loginResponse = authService.login(member);
		return ResponseEntity.ok(loginResponse);
	}

	@GetMapping("/{provider}/callback")
	public ResponseEntity<Map<String, String>> callBack(@PathVariable("provider") String provider,
			@RequestParam("code") String code, @RequestParam(value = "state", required = false) String state) {

		log.info("콜백 code={}, state={}", code, state);

		if (provider.equals("naver")) {
			Map<String, String> loginResponse = socialAuthService.socialLogin(code, state, provider);
			log.info("profile : {}", loginResponse);

			return ResponseEntity.ok(loginResponse);
		}

		if (provider.equals("kakao")) {
			log.info(provider);
			Map<String, String> userInfo = socialAuthService.findKakaoUserId(code);

			log.info("이거맞음? : {}", userInfo);

			int result = socialAuthService.checkUserById(userInfo);

			if (result < 1) {
				// DB에 없으면 회원가입 필요
				return ResponseEntity.status(200)
						.body(Map.of("message", "회원가입 필요", "userId", userInfo.get("id"), "refreshToken",
								userInfo.get("refreshToken"), "accessToken", userInfo.get("accessToken"), "provider",
								provider));
			} else {

				Map<String, String> loginResponse = socialAuthService.loginById(userInfo);

				// log.info("555555555{}",loginResponse);

				return ResponseEntity.ok(loginResponse);
			}

		}

		return ResponseEntity.badRequest().build();
	} // 홈으로 이동

	@PostMapping("/refresh")
	public ResponseEntity<Map<String, String>> refresh(@RequestBody Map<String, String> token) {

		String refreshToken = token.get("refreshToken");
		Map<String, String> tokens = tokenService.validateToken(refreshToken);

		return ResponseEntity.status(HttpStatus.CREATED).body(tokens);

	}

}
