package com.kh.pcar.back.admin.user.model.service;

import com.kh.pcar.back.admin.user.model.dto.UserDTO;
import com.kh.pcar.back.admin.user.model.dto.UserPageResponseDTO;

public interface UserService {

	UserPageResponseDTO findAllMember(int currentPage);
	
	void deleteUser(Long userNo);

	void updateUser(UserDTO userDTO);

	UserDTO findUserByNo(Long userNo);
}
