package com.kh.pcar.back.auth.model.service;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.kh.pcar.back.auth.model.dto.NaverProfileDTO;
import com.kh.pcar.back.auth.model.vo.NaverProfileVO;
import com.kh.pcar.back.exception.KakaoAuthException;
import com.kh.pcar.back.exception.NaverAuthException;
import com.kh.pcar.back.member.model.dao.MemberMapper;
import com.kh.pcar.back.member.model.dto.KakaoMemberDTO;
import com.kh.pcar.back.member.model.service.MemberService;
import com.kh.pcar.back.token.model.service.TokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SocialAuthServiceImpl implements SocialAuthService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final MemberService memberService;
    private final MemberMapper memberMapper;
    private final TokenService tokenService; // ✅ 추가

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

    /* ---------------------- Naver ---------------------- */

    private Map<String, Object> getNaverTokens(String code, String state) {
        if (code == null || code.isBlank()) throw new NaverAuthException("인가 코드(code)가 누락되었습니다.");
        if (state == null || state.isBlank()) throw new NaverAuthException("state 값이 누락되었습니다.");

        try {
            String url = "https://nid.naver.com/oauth2.0/token"
                    + "?grant_type=authorization_code"
                    + "&client_id=" + naverClientId
                    + "&client_secret=" + naverSecretCode
                    + "&code=" + code
                    + "&state=" + state;

            Map<String, Object> response = restTemplate.getForObject(URI.create(url), Map.class);
            if (response == null || !response.containsKey("access_token")) {
                throw new NaverAuthException("네이버 토큰 요청 실패");
            }
            return response;
        } catch (Exception e) {
            log.error("Naver 토큰 요청 중 오류 발생", e);
            throw new NaverAuthException("네이버 로그인 처리 중 문제가 발생했습니다.");
        }
    }

    @Override
    public Map<String, String> socialLogin(String code, String state, String provider) {
        Map<String, Object> tokens = getNaverTokens(code, state);
        String naverAccessToken = (String) tokens.get("access_token");
        String naverRefreshToken = (String) tokens.get("refresh_token");

        String profileUri = "https://openapi.naver.com/v1/nid/me";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + naverAccessToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        Map<String, Object> response;
        try {
            ResponseEntity<Map> respEntity = restTemplate.exchange(profileUri, HttpMethod.GET, entity, Map.class);
            Map<String, Object> body = respEntity.getBody();
            if (body == null || !body.containsKey("response")) {
                throw new NaverAuthException("네이버 프로필 정보를 가져올 수 없습니다.");
            }
            response = (Map<String, Object>) body.get("response");
        } catch (Exception e) {
            log.error("Naver 프로필 요청 중 오류 발생", e);
            throw new NaverAuthException("네이버 프로필 조회 실패");
        }

        NaverProfileDTO profileDTO = new NaverProfileDTO(
                null,
                (String) response.get("id"),
                (String) response.get("name"),
                (String) response.get("email"),
                (String) response.get("birthyear") + "-" + (String) response.get("birthday"),
                (String) response.get("mobile"),
                naverAccessToken,
                naverRefreshToken,
                provider,
                "ROLE_USER"
        );

        NaverProfileDTO npd = memberService.socialJoin(profileDTO);

        NaverProfileVO vo = NaverProfileVO.builder()
                .userNo(npd.getUserNo())
                .name(npd.getName())
                .id(npd.getId())
                .email(npd.getEmail())
                .mobile(npd.getMobile())
                .role(npd.getRole())
                .accessToken(naverAccessToken)
                .refreshtoken(naverRefreshToken)
                .birthday(npd.getBirthday())
                .provider(npd.getProvider())
                .build();

        return getLoginResponse(vo);
    }

    private Map<String, String> getLoginResponse(NaverProfileVO user) {
        // ✅ TokenService로 우리 JWT 발급!
        Map<String, String> loginResponse = tokenService.generateToken(
            user.getId(),      // username (네이버 ID)
            user.getUserNo(),  // userNo
            user.getRole()     // role
        );
        
        // 사용자 정보 추가
        loginResponse.put("userId", user.getId());
        loginResponse.put("userNo", String.valueOf(user.getUserNo()));
        loginResponse.put("birthDay", user.getBirthday());
        loginResponse.put("userName", user.getName());
        loginResponse.put("email", user.getEmail());
        loginResponse.put("phone", user.getMobile());
        loginResponse.put("role", user.getRole());
        loginResponse.put("provider", user.getProvider());
        // ⭐ accessToken, refreshToken은 이미 tokenService.generateToken()에서 추가됨
        
        return loginResponse;
    }

    /* ---------------------- Kakao ---------------------- */

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
        Map<String, Object> response;
        try {
            response = restTemplate.postForObject(tokenUrl, request, Map.class);
            if (response == null || !response.containsKey("access_token")) {
                throw new KakaoAuthException("카카오 토큰 요청 실패");
            }
        } catch (Exception e) {
            log.error("카카오 토큰 요청 중 오류 발생", e);
            throw new KakaoAuthException("카카오 로그인 처리 중 문제가 발생했습니다.");
        }

        String kakaoAccessToken = (String) response.get("access_token");
        String kakaoRefreshToken = (String) response.get("refresh_token");

        // 유저 정보 조회
        HttpHeaders userHeaders = new HttpHeaders();
        userHeaders.setBearerAuth(kakaoAccessToken);
        HttpEntity<Void> userRequest = new HttpEntity<>(userHeaders);

        Map<String, Object> userInfo;
        try {
            userInfo = restTemplate.exchange(
                    "https://kapi.kakao.com/v2/user/me",
                    HttpMethod.GET,
                    userRequest,
                    Map.class
            ).getBody();
            if (userInfo == null) {
                throw new KakaoAuthException("카카오 사용자 정보 조회 실패");
            }
        } catch (Exception e) {
            log.error("카카오 프로필 요청 중 오류 발생", e);
            throw new KakaoAuthException("카카오 사용자 정보 조회 실패");
        }

        log.info("kakao userInfo : {}", userInfo);

        return Map.of(
                "id", String.valueOf(userInfo.get("id")),
                "kakaoAccessToken", kakaoAccessToken,
                "kakaoRefreshToken", kakaoRefreshToken
        );
    }

    public int checkUserById(Map<String, String> userInfo) {
        String id = userInfo.get("id");
        return memberMapper.countByMemberId(id);
    }

    public Map<String, String> loginById(Map<String, String> userInfo) {
        KakaoMemberDTO member = memberMapper.findByUserId(userInfo.get("id"));
        if (member == null) throw new KakaoAuthException("등록되지 않은 카카오 사용자입니다.");

        // 카카오 토큰은 참고용 (선택사항)
        member.setAccessToken(userInfo.get("kakaoAccessToken"));
        member.setRefreshToken(userInfo.get("kakaoRefreshToken"));

        return getLoginResponse(member);
    }

    private Map<String, String> getLoginResponse(KakaoMemberDTO member) {
        // ✅ TokenService로 우리 JWT 발급!
        Map<String, String> loginResponse = tokenService.generateToken(
            member.getMemberId(),  // username (카카오 ID)
            member.getUserNo(),    // userNo
            member.getRole()       // role
        );
        
        // 사용자 정보 추가
        loginResponse.put("userId", member.getMemberId());
        loginResponse.put("userNo", String.valueOf(member.getUserNo()));
        loginResponse.put("birthDay", member.getBirthDay());
        loginResponse.put("userName", member.getMemberName());
        loginResponse.put("email", member.getEmail());
        loginResponse.put("phone", member.getPhone());
        loginResponse.put("role", member.getRole());
        loginResponse.put("licenseImg", member.getLicenseUrl());
        loginResponse.put("provider", member.getProvider());
        // ⭐ accessToken, refreshToken은 이미 tokenService.generateToken()에서 추가됨
        
        return loginResponse;
    }
}