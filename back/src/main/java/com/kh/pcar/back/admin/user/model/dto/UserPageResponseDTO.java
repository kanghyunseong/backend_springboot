package com.kh.pcar.back.admin.user.model.dto;

import java.util.List;

import com.kh.pcar.back.util.PageInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPageResponseDTO {
	
	private PageInfo pageInfo;
	private List<UserDTO> users;

}
