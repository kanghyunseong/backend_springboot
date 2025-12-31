package com.kh.pcar.back.member.model.service;


import org.springframework.web.multipart.MultipartFile;

import com.kh.pcar.back.auth.model.dto.NaverProfileDTO;
import com.kh.pcar.back.auth.model.vo.CustomUserDetails;
import com.kh.pcar.back.member.model.dto.ChangePasswordDTO;
import com.kh.pcar.back.member.model.dto.KakaoMemberDTO;
import com.kh.pcar.back.member.model.dto.MemberDTO;
import com.kh.pcar.back.member.model.dto.MemberUpdateDTO;

public interface MemberService {

	void join(MemberDTO member, MultipartFile licenseImg);

	void kakaoJoin(KakaoMemberDTO member, MultipartFile licenseImg);

	NaverProfileDTO socialJoin(NaverProfileDTO naverMember);

	void changePassword(ChangePasswordDTO password, CustomUserDetails userDetails);

	void deleteByPassword(String inputPassword, CustomUserDetails userDetails);

	MemberDTO updateUser(MemberUpdateDTO member, MultipartFile licenseImg, CustomUserDetails userDetails);
}
