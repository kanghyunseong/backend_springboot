package com.kh.pcar.back.member.model.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.kh.pcar.back.auth.model.dto.NaverProfileDTO;
import com.kh.pcar.back.auth.model.vo.NaverProfileVO;
import com.kh.pcar.back.member.model.dto.MemberDTO;
import com.kh.pcar.back.member.model.vo.MemberVO;

@Mapper
public interface MemberMapper {

	@Select("SELECT COUNT(*) FROM TB_MEMBER WHERE USER_ID = #{memberId}")
	int countByMemberId(String memberId);

	int socialJoin(NaverProfileVO member);

	@Insert("INSERT INTO TB_SOCIAL (USER_NO,PROVIDER) VALUES (SEQ_MEMBER.CURRVAL, #{provider})")
	int joinSocial(NaverProfileVO member);

	int join(MemberVO member);

	@Insert("INSERT INTO TB_LOCAL (USER_NO,PASSWORD) VALUES (SEQ_MEMBER.CURRVAL, #{memberPwd})")
	int joinLocal(MemberVO member);

	@Select("Select USER_NO FROM TB_MEMBER WHERE USER_ID = #{memberId}")
	Long findUserNoById(String memberId);
	
	MemberDTO loadUser(String userId);

	void socialJoin(NaverProfileDTO naverMember);

	void joinSocial(NaverProfileDTO naverMember);
}
