package com.kh.pcar.back.auth.model.vo;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class NaverProfileVO {

	private String id;
	private String name;
	private String email;
	private String birthday;
	private String mobile;
	private String licenseUrl;
	private String role;
	private String accessToken;
	private String refreshtoken;
	private String provider;
	private Long userNo;
}