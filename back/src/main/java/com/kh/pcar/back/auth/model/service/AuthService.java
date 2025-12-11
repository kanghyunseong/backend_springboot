package com.kh.pcar.back.auth.model.service;

import java.util.Map;

import com.kh.pcar.back.auth.model.dto.MemberLoginDTO;

public interface AuthService {
	Map<String, String> login(MemberLoginDTO member);
}
