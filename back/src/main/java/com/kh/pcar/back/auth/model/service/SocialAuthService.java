package com.kh.pcar.back.auth.model.service;

import java.util.Map;

import com.kh.pcar.back.auth.model.vo.NaverProfileVO;

public interface SocialAuthService {

	public Map<String, String> findKakaoUserId(String code);

	public Map<String, String> socialLogin(String code, String state, String provider);

	public Map<String, String> loginById(Map<String, String> userInfo);

	public int checkUserById(Map<String, String> userInfo);

	
	public Map<String, String> processCallback(String provider, String code, String state);
}
