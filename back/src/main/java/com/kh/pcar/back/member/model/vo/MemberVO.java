package com.kh.pcar.back.member.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@Builder
@ToString
public class MemberVO {

	private String memberId;
	private String memberPwd;
	private String memberName;
	private String birthDay;
	private String email;
	private String phone;
	private String licenseUrl;
	private String role;
}
