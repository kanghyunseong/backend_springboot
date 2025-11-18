package com.kh.pcar.back.admin.user.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.kh.pcar.back.admin.user.model.dto.UserDTO;

@Mapper
public interface UserMapper {
	
	// RowBounds를 매개변수로 받아 해당 페이지의 목록만 조회
		List<UserDTO> findAllMember(RowBounds rowBounds);
	    
	    // 페이징 계산을 위한 전체 유저 수 조회
	    int getTotalCount();

}
