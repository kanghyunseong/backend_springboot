package com.kh.pcar.back.admin.user.model.dto;



import java.util.Date;

import com.kh.pcar.back.admin.user.model.vo.UserVO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDTO {
	
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
	private String licenseStatus;

}
