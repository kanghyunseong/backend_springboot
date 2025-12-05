package com.kh.pcar.back.member.model.vo;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class KakaoMemberVO {

	private Long userNo;
	private String memberId;
	private String memberName;
	private String birthDay;
	private String email;
	private String phone;
	private String licenseUrl;
	private String role;
	private String provider;
}
