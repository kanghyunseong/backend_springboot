package com.kh.pcar.back.admin.user.model.vo;

import java.sql.Date;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserVO {
	
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
