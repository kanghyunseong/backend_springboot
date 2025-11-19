package com.kh.pcar.back.auth.model.controller;

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
import com.kh.pcar.back.auth.model.vo.NaverProfileVO;
import com.kh.pcar.back.token.model.service.TokenService;

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
	public ResponseEntity<?> login(@Valid @RequestBody MemberLoginDTO member) {
	    Map<String, String> loginResponse = authService.login(member);
	    return ResponseEntity.ok(loginResponse);
	}
	
	@GetMapping("/kakao")
	public ResponseEntity<?> kakaoLogin(){
		
		
		return null;
	}
	
	  @GetMapping("/naver")
	    public ResponseEntity<?> naverAuth() {
	        String url = socialAuthService.requestNaver();
	        return ResponseEntity.ok(url); 
	    }
	   
	  
	  
	  
	  @GetMapping("/{provider}/callback")
	    public ResponseEntity<?> callBackNaver( @PathVariable("provider") String provider,@RequestParam("code") String code, @RequestParam("state") String state) {
	      
		   log.info("콜백 code={}, state={}", code, state);
		   
		   if(provider.equals("naver")) {
			   Map<String, String> loginResponse = socialAuthService.socialLogin(code, state,provider);
		        log.info("profile : {}" , loginResponse);
		        // DB에 provider="naver" 저장
		        return ResponseEntity.ok(loginResponse);
		    }
	        

	        // TODO: 회원가입/로그인 처리 후 세션/쿠키 저장 가능

	        return null;  // 홈으로 이동
	    }
	
	@PostMapping("/refresh")
	public ResponseEntity<?> refresh(@RequestBody Map<String,String> token){
		
		String refreshToken = token.get("refreshToken");
		Map<String,String> tokens = tokenService.validateToken(refreshToken);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(token);
		
	}
	
}
