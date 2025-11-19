package com.kh.pcar.back.auth.model.service;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import com.kh.pcar.back.auth.model.vo.NaverProfileVO;


import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SocialAuthServiceImpl implements SocialAuthService {

	private RestTemplate restTemplate = new RestTemplate();

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

	@Override
	public String getAccessToken(String code, String state) {
		String naverTokenUri = "https://nid.naver.com/oauth2.0/token";
		String url = naverTokenUri + "?grant_type=authorization_code" + "&client_id=" + naverClientId
				+ "&client_secret=" + naverSecretCode + "&code=" + code + "&state=" + state;

		log.info("토큰 요청 URL: {}", url);

		// 요청 보내기
		Map<String, Object> response = restTemplate.getForObject(URI.create(url), Map.class);
		log.info("토큰 응답: {}", response);

		return (String) response.get("access_token");
	}

	@Override
	public NaverProfileVO getProfile(String accessToken) {

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
		        .birthday((String) responseBody.get("birthday"))
		        .birthyear((String) responseBody.get("birthyear"))
		        .mobile((String) responseBody.get("mobile"))
		        .build();

		return profile;
	}

}
