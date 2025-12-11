package com.kh.pcar.back.member.model.vo;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MemberUpdateVO {
	private Long userNo; // 로그인한 유저 PK
	private String memberName;
	private String email;
	private String phone;
	private String licenseUrl;
}