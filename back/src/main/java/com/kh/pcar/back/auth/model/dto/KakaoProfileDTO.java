package com.kh.pcar.back.auth.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class KakaoProfileDTO {

	private Long UserNo;
	private String id;
	private String name;
	private String email;
	private String birthday;
	private String mobile;
	private String role;
	private String provider;

}
