package com.kh.pcar.back.auth.model.service;

import java.util.Map;

import com.kh.pcar.back.auth.model.vo.NaverProfileVO;

public interface SocialAuthService {
	public String requestNaver();
	
	
	
	 public Map<String,String> socialLogin(String code,String state,String provider);
	 

}
