package com.kh.pcar.back.member.model.service;

import org.springframework.web.multipart.MultipartFile;

import com.kh.pcar.back.member.model.dto.MemberDTO;



public interface MemberService {

	
	void join(MemberDTO member,MultipartFile licenseImg);
	
}
