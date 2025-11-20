package com.kh.pcar.back.member.model.service;

import org.springframework.web.multipart.MultipartFile;

import com.kh.pcar.back.auth.model.dto.NaverProfileDTO;
import com.kh.pcar.back.auth.model.vo.NaverProfileVO;
import com.kh.pcar.back.member.model.dto.MemberDTO;



public interface MemberService {

	
	void join(MemberDTO member,MultipartFile licenseImg);
	
	
	NaverProfileDTO socialJoin(NaverProfileDTO naverMember);
}
