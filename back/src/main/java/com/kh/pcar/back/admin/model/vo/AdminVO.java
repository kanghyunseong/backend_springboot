package com.kh.pcar.back.admin.model.vo;

import java.sql.Date;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class AdminVO {
	
	private Long userNo;
	private String userId;
	private String userName;
	private String birthday;
	private String email;
	private String phone;
	private String userRoll;
	private String licenseImg;
	private String status;
	private Date enrollDate;

}
