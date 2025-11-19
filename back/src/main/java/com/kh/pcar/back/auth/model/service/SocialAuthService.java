package com.kh.pcar.back.auth.model.service;

import java.util.Map;

public interface SocialAuthService {
	public String requestNaver();
	
	
	
	 public Map<String,String> socialLogin(String code,String state,String provider);
}
