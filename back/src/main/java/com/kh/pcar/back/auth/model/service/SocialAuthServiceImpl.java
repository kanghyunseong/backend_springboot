package com.kh.pcar.back.auth.model.service;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.kh.pcar.back.auth.model.dto.NaverProfileDTO;
import com.kh.pcar.back.auth.model.vo.NaverProfileVO;
import com.kh.pcar.back.member.model.dao.MemberMapper;
import com.kh.pcar.back.member.model.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SocialAuthServiceImpl implements SocialAuthService {

	private RestTemplate restTemplate = new RestTemplate();
	private final MemberService memberService;
	private final MemberMapper memberMapper;
	
	
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

		  NaverProfileDTO profileDTO = new NaverProfileDTO(
				    null,
			        (String) responseBody.get("id"),
			        (String) responseBody.get("name"),
			        (String) responseBody.get("email"),
			        (String) responseBody.get("birthyear") + "-" + (String) responseBody.get("birthday"),
			        (String) responseBody.get("mobile"),
			        accessToken,
			        refreshToken
			        ,provider
			        ,"ROLE_USER"
			    );

		
		  NaverProfileDTO npd =	memberService.socialJoin(profileDTO);
		  
		  
		  
		  NaverProfileVO nv = NaverProfileVO.builder()
				  			.userNo(npd.getUserNo())
				  			.name(npd.getName())
				  			.id(npd.getId())
				  			.email(npd.getEmail())
				  			.mobile(npd.getMobile())
				  			.role(npd.getRole())
				  			.accessToken(npd.getAccessToken())
				  			.refreshtoken(npd.getRefreshtoken())
				  			.birthday(npd.getBirthday())
				  			.provider(npd.getProvider())
				  			.build();
				  		    
		  
		
		return getLoginResponse(nv);
	}
	
	private Map<String,String> getLoginResponse(NaverProfileVO user){
		
		Map<String, String> loginResponse = new HashMap<>();
		
		loginResponse.put("userId",user.getId());
		loginResponse.put("userNo", String.valueOf(user.getUserNo()));
		loginResponse.put("birthDay", user.getBirthday());
		loginResponse.put("userName", user.getName());
		loginResponse.put("email", user.getEmail());
		loginResponse.put("phone", user.getMobile());
		loginResponse.put("role", user.getRole().toString());
		loginResponse.put("accessToken", user.getAccessToken());
		loginResponse.put("refreshToken", user.getRefreshtoken());
		loginResponse.put("provider", user.getProvider());
		
		
		return loginResponse;
		
	}


}
