package com.kh.pcar.back.member.model.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kh.pcar.back.auth.model.dto.NaverProfileDTO;
import com.kh.pcar.back.auth.model.vo.NaverProfileVO;
import com.kh.pcar.back.exception.IdDuplicateException;
import com.kh.pcar.back.exception.MemberJoinException;
import com.kh.pcar.back.file.service.FileService;
import com.kh.pcar.back.member.model.dao.MemberMapper;
import com.kh.pcar.back.member.model.dto.KakaoMemberDTO;
import com.kh.pcar.back.member.model.dto.MemberDTO;
import com.kh.pcar.back.member.model.vo.KakaoMemberVO;
import com.kh.pcar.back.member.model.vo.MemberVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class MemberServiceImpl implements MemberService {

	private final MemberMapper mapper;
	private final FileService fileService;
	private final PasswordEncoder passwordEncoder;
	
	// 아이디 중복체크 메소드 
	private void checkId(String id) {
		
		int count = mapper.countByMemberId(id);
		
		if(count == 1) {
			throw new IdDuplicateException("이미 존재하는 아이디입니다.");
		}
		
	}
	
	private MemberVO generateFileName(MemberDTO member,MultipartFile licenseImg) {
		MemberVO m = null;
		
		if(licenseImg != null && !licenseImg.isEmpty()) {
			
			String filePath = fileService.store(licenseImg);
			
			m = MemberVO.builder().memberId(member.getMemberId())
						          .memberPwd(passwordEncoder.encode(member.getMemberPwd()))
						          .memberName(member.getMemberName())
						          .licenseUrl(filePath)
						          .birthDay(member.getBirthDay())
						          .email(member.getEmail())
						          .phone(member.getPhone())
						          .role("ROLE_USER").build();
			return m;
		}else {
			m = MemberVO.builder().memberId(member.getMemberId())
			          .memberPwd(passwordEncoder.encode(member.getMemberPwd()))
			          .memberName(member.getMemberName())
			          .birthDay(member.getBirthDay())
			          .email(member.getEmail())
			          .phone(member.getPhone())
			          .role("ROLE_USER").build();
			
			return m;
		}
		
	}
	
	// 회원가입 
	@Override
	@Transactional
	public void join(MemberDTO member, MultipartFile licenseImg) {
		
		checkId(member.getMemberId());
		
		MemberVO originMember =  generateFileName(member,licenseImg);
		
		//log.info("originMember {} " , originMember);
		
		int result = mapper.join(originMember);
		
		if(result <= 0) {
			throw new MemberJoinException("회원가입에 실패했습니다");
		}
		
		mapper.joinLocal(originMember);
	}
	
	@Override
	@Transactional
	public NaverProfileDTO socialJoin(NaverProfileDTO naverMember) {
		
		int count = mapper.countByMemberId(naverMember.getId());
		
		
		if(count > 0) {
		Long userNo = mapper.findUserNoById(naverMember.getId());
			naverMember.setUserNo(userNo);
			
			return naverMember;
		}
		else {
			
			mapper.socialJoin(naverMember);
			mapper.joinSocial(naverMember);
			Long userNo= 	mapper.findUserNoById(naverMember.getId());
			naverMember.setUserNo(userNo);
			
			
			return naverMember;
		}
		
	}
	
	
	
	
	@Override
	@Transactional
	public Map<String,String> kakaoJoin(KakaoMemberDTO member,MultipartFile licenseImg) {
	
		
			log.info("222222222222222{}" , member);
		
			member.setRole("ROLE_USER");
			generateFileName(member , licenseImg);
			log.info("33333333333{}" , member);
			mapper.kakaoJoin(member);
			mapper.kakaoProviderJoin(member);

			
			KakaoMemberDTO kakaoMember = mapper.findByUserId(member.getMemberId());
			
			
			
			log.info("4444444{}",kakaoMember);
			return null;
		
		
	}
	
	
private Map<String,String> getKakaoLoginResponse(KakaoMemberVO user){
		
		Map<String, String> loginResponse = new HashMap<>();
		
		loginResponse.put("userId",user.getMemberId());
		loginResponse.put("userNo", String.valueOf(user.getUserNo()));
		loginResponse.put("birthDay", user.getBirthDay());
		loginResponse.put("userName", user.getMemberName());
		loginResponse.put("email", user.getEmail());
		loginResponse.put("phone", user.getPhone());
		loginResponse.put("provider", user.getProvider());
		loginResponse.put("role", user.getRole().toString());
		
		
		return loginResponse;
		
	}

	
private KakaoMemberDTO generateFileName(KakaoMemberDTO member,MultipartFile licenseImg) {
	
	
	if(licenseImg != null && !licenseImg.isEmpty()) {
		
		String filePath = fileService.store(licenseImg);
		
		member.setLicenseUrl(filePath);
		
		return member;
	}
	return null;
}
	
	

}
