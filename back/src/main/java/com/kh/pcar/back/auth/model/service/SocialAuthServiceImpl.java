package com.kh.pcar.back.auth.model.service;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.kh.pcar.back.auth.model.dto.MemberLoginDTO;
import com.kh.pcar.back.auth.model.vo.CustomUserDetails;
import com.kh.pcar.back.auth.model.vo.NaverProfileVO;
import com.kh.pcar.back.member.model.service.MemberService;
import com.kh.pcar.back.member.model.service.MemberServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SocialAuthServiceImpl implements SocialAuthService {

	private RestTemplate restTemplate = new RestTemplate();
	private final MemberService memberService;
	
	
	@Value("${naver.client.id}")
	private String naverClientId;

	@Value("${naver.client.secret}")
	private String naverSecretCode;

	@Value("${naver.redirect.url}")
	private String naverredirectUrl;

	@Override
	public String requestNaver() {
		String naver_auth_uri = "https://nid.naver.com/oauth2.0/authorize";

		String state = "state_" + System.currentTimeMillis() + "_" + (int) (Math.random() * 100000);

		String url = naver_auth_uri + "?response_type=code" + "&client_id=" + naverClientId + "&redirect_uri="
				+ URLEncoder.encode(naverredirectUrl, StandardCharsets.UTF_8) + "&state=" + state;

		log.info("네이버 로그인 URL 생성됨 → {}", url);
		return url;

	}


	private Map<String, Object> getTokens(String code, String state) {
	    String naverTokenUri = "https://nid.naver.com/oauth2.0/token";
	    String url = naverTokenUri + "?grant_type=authorization_code"
	            + "&client_id=" + naverClientId
	            + "&client_secret=" + naverSecretCode
	            + "&code=" + code
	            + "&state=" + state;

	   // log.info("토큰 요청 URL: {}", url);

	    // 요청 보내기
	    Map<String, Object> response = restTemplate.getForObject(URI.create(url), Map.class);
	  //  log.info("토큰 응답: {}", response);

	    return response; // access_token, refresh_token 둘 다 포함됨
	}
	

	
	@Override
	public Map<String,String> socialLogin(String code,String state,String provider) {
		Map<String, Object> tokens = getTokens(code, state);
		String accessToken = (String) tokens.get("access_token");
		String refreshToken = (String) tokens.get("refresh_token");
		
		//log.info("accessToken : {} , refreshToken : {} ", accessToken, refreshToken );
		

		String naverProfileUri = "https://openapi.naver.com/v1/nid/me";

		var headers = new org.springframework.http.HttpHeaders();
		headers.set("Authorization", "Bearer " + accessToken);

		var entity = new org.springframework.http.HttpEntity<>(headers);
		var response = restTemplate.exchange(naverProfileUri, org.springframework.http.HttpMethod.GET, entity,
				Map.class);

		Map<String, Object> responseBody = (Map<String, Object>) response.getBody().get("response");

		NaverProfileVO profile = NaverProfileVO.builder()
		        .id((String) responseBody.get("id"))
		        .name((String) responseBody.get("name"))
		        .email((String) responseBody.get("email"))
		        .birthday( (String) responseBody.get("birthyear")+"-"+(String) responseBody.get("birthday"))
		        .mobile((String) responseBody.get("mobile"))
		        .accessToken(accessToken)
		        .refreshtoken(refreshToken)
		        .provider(provider)
		        .role("ROLE_USER")
		        .build();

		memberService.socialJoin(profile);
		
		return getLoginResponse(profile);
	}
	
	private Map<String,String> getLoginResponse(NaverProfileVO user){
		
		Map<String, String> loginResponse = new HashMap<>();
		
		loginResponse.put("userId",user.getId());
		loginResponse.put("birthDay", user.getBirthday());
		loginResponse.put("userName", user.getName());
		loginResponse.put("email", user.getEmail());
		loginResponse.put("phone", user.getMobile());
		loginResponse.put("role", user.getRole().toString());
		loginResponse.put("accessToken", user.getRefreshtoken());
		loginResponse.put("refreshToken", user.getAccessToken());
		loginResponse.put("provider", user.getProvider());
		
		
		return loginResponse;
		
	}


}
