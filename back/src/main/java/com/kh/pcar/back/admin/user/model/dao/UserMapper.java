package com.kh.pcar.back.admin.user.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.kh.pcar.back.admin.user.model.dto.UserDTO;

@Mapper
public interface UserMapper {
	
		List<UserDTO> findAllMember(RowBounds rowBounds);
	    
	    int getTotalCount();
	    
	    int deleteUserStatus(Long userNo);

		int updateUser(UserDTO userDTO);

		UserDTO findByUserNo(Long userNo);

		List<Map<String, Object>> getJoinTrend(String unit);
		
		int getWaitingLicenseCount();

		List<Map<String, Object>> getLicenseStatusTrend(String unit);
}
