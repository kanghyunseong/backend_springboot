package com.kh.pcar.back.auth.model.service;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.kh.pcar.back.auth.model.dto.KakaoProfileDTO;
import com.kh.pcar.back.auth.model.dto.NaverProfileDTO;
import com.kh.pcar.back.auth.model.vo.NaverProfileVO;
import com.kh.pcar.back.member.model.dao.MemberMapper;
import com.kh.pcar.back.member.model.dto.KakaoMemberDTO;
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
	private String naverRedirectUrl;
	
	@Value("${kakao.client.id}")
	private String kakaoClientId;


	@Value("${kakao.redirect.url}")
	private String kakaoRedirectUrl;




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
	
	
	
	
	
	
	
	
	public Map<String, String> findKakaoUserId(String code) {
	    String tokenUrl = "https://kauth.kakao.com/oauth/token";

	    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
	    params.add("grant_type", "authorization_code");
	    params.add("client_id", kakaoClientId);
	    params.add("redirect_uri", kakaoRedirectUrl);
	    params.add("code", code);

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

	    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

	    RestTemplate restTemplate = new RestTemplate();
	    Map<String, Object> response = restTemplate.postForObject(tokenUrl, request, Map.class);

	    String accessToken = (String) response.get("access_token");
	    String refreshToken = (String) response.get("refresh_token");

	    // accessToken으로 유저 정보 요청
	    HttpHeaders userHeaders = new HttpHeaders();
	    userHeaders.setBearerAuth(accessToken);

	    HttpEntity<Void> userRequest = new HttpEntity<>(userHeaders);
	    Map<String, Object> userInfo = restTemplate.exchange(
	            "https://kapi.kakao.com/v2/user/me",
	            HttpMethod.GET,
	            userRequest,
	            Map.class).getBody();

	    // userInfo에서 필요한 값 추출 후 DTO 또는 VO 만들고 DB 처리
	    // Map<String,String> loginResponse = getLoginResponse(vo);
	    
	    log.info("kakao userInfo : {}",userInfo);
	    log.info("kakao accessToken : {}",accessToken);
	    log.info("kakao refreshToken : {}",refreshToken);
	    
	    String id = String.valueOf(userInfo.get("id"));
	    
	    return Map.of(
	    	"id" , id,
	        "accessToken", accessToken,
	        "refreshToken", refreshToken
	    );
	}
	
	public int checkUserById(Map<String,String> userInfo){
		
		
		
	
		String id = userInfo.get("id");
		
		
		int result = memberMapper.countByMemberId(id);
		
		
		return result; 
		
		
	};
	
	public Map<String,String> loginById(Map<String,String> userInfo){
		
		
		KakaoMemberDTO member = memberMapper.findByUserId(userInfo.get("id"));
		
		member.setAccessToken(userInfo.get("accessToken"));
		member.setRefreshToken(userInfo.get("refreshToken"));
		
		
		
		Map<String,String> loginResponse = getLoginResponse(member);
		
		
		return loginResponse;
		
		
	}
	
	private Map<String, String> getLoginResponse(KakaoMemberDTO member) {
	    Map<String, String> loginResponse = new HashMap<>();

	    //log.info("member : {}" , member );
	    
	    loginResponse.put("userId", member.getMemberId());
	    loginResponse.put("userNo", String.valueOf(member.getUserNo()));
	    loginResponse.put("birthDay", member.getBirthDay());
	    loginResponse.put("userName", member.getMemberName());
	    loginResponse.put("email", member.getEmail());
	    loginResponse.put("phone", member.getPhone());
	    loginResponse.put("role", member.getRole());
	    loginResponse.put("refreshToken", member.getRefreshToken());
	    loginResponse.put("accessToken", member.getAccessToken());
	    loginResponse.put("licenseImg", member.getLicenseUrl());
	    loginResponse.put("provider", member.getProvider());

	    return loginResponse;
	}

}
