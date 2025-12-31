package com.kh.pcar.back.member.model.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.kh.pcar.back.auth.model.dto.NaverProfileDTO;
import com.kh.pcar.back.auth.model.vo.NaverProfileVO;
import com.kh.pcar.back.member.model.dto.KakaoMemberDTO;
import com.kh.pcar.back.member.model.dto.MemberDTO;
import com.kh.pcar.back.member.model.vo.MemberUpdateVO;
import com.kh.pcar.back.member.model.vo.MemberVO;

@Mapper
public interface MemberMapper {

	@Select("SELECT COUNT(*) FROM TB_MEMBER WHERE USER_ID = #{memberId}")
	int countByMemberId(String memberId);

	int socialJoin(NaverProfileVO member);

	@Insert("INSERT INTO TB_SOCIAL (USER_NO,PROVIDER) VALUES (SEQ_MEMBER.CURRVAL, #{provider})")
	int joinSocial(NaverProfileVO member);

	int join(MemberVO member);

	int kakaoJoin(KakaoMemberDTO member);

	@Insert("INSERT INTO TB_SOCIAL (USER_NO,PROVIDER) VALUES (SEQ_MEMBER.CURRVAL, #{provider})")
	int kakaoProviderJoin(KakaoMemberDTO member);

	@Insert("INSERT INTO TB_LOCAL (USER_NO,PASSWORD) VALUES (SEQ_MEMBER.CURRVAL, #{memberPwd})")
	int joinLocal(MemberVO member);

	@Select("Select USER_NO FROM TB_MEMBER WHERE USER_ID = #{memberId}")
	Long findUserNoById(String memberId);
	
	NaverProfileDTO findByNaverId(String userId);

	MemberDTO loadUser(String userId);

	void socialJoin(NaverProfileDTO naverMember);

	void joinSocial(NaverProfileDTO naverMember);

	KakaoMemberDTO findByUserId(String id);

	@Update("UPDATE TB_LOCAL SET PASSWORD = #{newPassword} WHERE USER_NO = #{userNo}")
	void changePassword(Map<String, Object> changeRequest);

	@Update("UPDATE TB_MEMBER SET STATUS = 'N' WHERE USER_NO = #{userNo} ")
	void deleteUserNo(String userNo);

	void updateUser(Map<String, Object> updateParam);
}
