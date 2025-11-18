package com.kh.pcar.back.admin.user.model.service;

import com.kh.pcar.back.admin.user.model.dto.UserPageResponseDTO;

public interface UserService {

	UserPageResponseDTO findAllMember(int currentPage);
	
	void deleteUser(Long userNo);
}
