package com.kh.pcar.back.member.dao;

import org.apache.ibatis.annotations.Mapper;
import com.kh.pcar.back.member.dto.MemberDTO;

@Mapper
public interface MemberMapper {

	
	MemberDTO loadUser(String memberId);
}
