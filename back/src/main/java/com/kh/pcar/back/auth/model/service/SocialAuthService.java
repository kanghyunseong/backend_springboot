package com.kh.pcar.back.auth.model.service;

import com.kh.pcar.back.auth.model.dto.NaverProfileDTO;
import com.kh.pcar.back.auth.model.vo.NaverProfileVO;

public interface SocialAuthService {
	public String requestNaver();
	
	public String getAccessToken(String code, String state);
	
	 public NaverProfileVO getProfile(String accessToken);
}
