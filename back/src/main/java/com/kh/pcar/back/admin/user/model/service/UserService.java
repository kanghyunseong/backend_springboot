package com.kh.pcar.back.admin.user.model.service;

import java.util.List;
import java.util.Map;

import com.kh.pcar.back.admin.user.model.dto.UserDTO;
import com.kh.pcar.back.admin.user.model.dto.UserKpiStatsDTO;
import com.kh.pcar.back.admin.user.model.dto.UserPageResponseDTO;

public interface UserService {

	UserPageResponseDTO findAllMember(int currentPage);
	
	void deleteUser(Long userNo);

	UserDTO updateUser(UserDTO userDTO);

	UserDTO findUserByNo(Long userNo);

	List<Map<String, Object>> getJoinTrend(String unit);
	
	UserKpiStatsDTO getKpiStats();
	
	List<Map<String, Object>> getLicenseStatusTrend(String unit);

	
}
