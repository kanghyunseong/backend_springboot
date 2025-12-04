package com.kh.pcar.back.member.model.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kh.pcar.back.auth.model.dto.NaverProfileDTO;
import com.kh.pcar.back.auth.model.vo.CustomUserDetails;
import com.kh.pcar.back.exception.CustomAuthenticationException;
import com.kh.pcar.back.exception.FileUploadException;
import com.kh.pcar.back.exception.IdDuplicateException;
import com.kh.pcar.back.exception.MemberJoinException;
import com.kh.pcar.back.file.service.FileService;
import com.kh.pcar.back.member.model.dao.MemberMapper;
import com.kh.pcar.back.member.model.dto.ChangePasswordDTO;
import com.kh.pcar.back.member.model.dto.KakaoMemberDTO;
import com.kh.pcar.back.member.model.dto.MemberDTO;
import com.kh.pcar.back.member.model.dto.MemberUpdateDTO;
import com.kh.pcar.back.member.model.vo.MemberVO;
import com.kh.pcar.back.token.model.dao.TokenMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class MemberServiceImpl implements MemberService {

	private final MemberMapper mapper;
	private final FileService fileService;
	private final PasswordEncoder passwordEncoder;
	private final TokenMapper tokenMapper;

	// 아이디 중복체크 메소드
	private void checkId(String id) {

		int count = mapper.countByMemberId(id);

		if (count == 1) {
			throw new IdDuplicateException("이미 존재하는 아이디입니다.");
		}

	}

	private MemberVO generateFileName(MemberDTO member, MultipartFile licenseImg) {
		MemberVO m = null;

		if (licenseImg != null && !licenseImg.isEmpty()) {

			String filePath = fileService.store(licenseImg);

			m = MemberVO.builder().memberId(member.getMemberId())
					.memberPwd(passwordEncoder.encode(member.getMemberPwd())).memberName(member.getMemberName())
					.licenseUrl(filePath).birthDay(member.getBirthDay()).email(member.getEmail())
					.phone(member.getPhone()).role("ROLE_USER").build();
			return m;
		} else {
			m = MemberVO.builder().memberId(member.getMemberId())
					.memberPwd(passwordEncoder.encode(member.getMemberPwd())).memberName(member.getMemberName())
					.birthDay(member.getBirthDay()).email(member.getEmail()).phone(member.getPhone()).role("ROLE_USER")
					.build();

			return m;
		}

	}

	// 회원가입
	@Override
	@Transactional
	public void join(MemberDTO member, MultipartFile licenseImg) {

		checkId(member.getMemberId());

		MemberVO originMember = generateFileName(member, licenseImg);

		// log.info("originMember {} " , originMember);

		int result = mapper.join(originMember);

		if (result <= 0) {
			throw new MemberJoinException("회원가입에 실패했습니다");
		}

		mapper.joinLocal(originMember);
	}

	@Override
	@Transactional
	public NaverProfileDTO socialJoin(NaverProfileDTO naverMember) {

		int count = mapper.countByMemberId(naverMember.getId());

		if (count > 0) {
			Long userNo = mapper.findUserNoById(naverMember.getId());
			naverMember.setUserNo(userNo);

			return naverMember;
		} else {
			
			mapper.socialJoin(naverMember);
			mapper.joinSocial(naverMember);
			Long userNo = mapper.findUserNoById(naverMember.getId());
			naverMember.setUserNo(userNo);

			return naverMember;
		}

	}

	@Override
	@Transactional
	public Map<String, String> kakaoJoin(KakaoMemberDTO member, MultipartFile licenseImg) {

		// log.info("222222222222222{}" , member);

		member.setRole("ROLE_USER");
		generateFileName(member, licenseImg);
		// log.info("33333333333{}" , member);
		mapper.kakaoJoin(member);
		mapper.kakaoProviderJoin(member);

		KakaoMemberDTO kakaoMember = mapper.findByUserId(member.getMemberId());

		// log.info("4444444{}",kakaoMember);
		return null;

	}

//private Map<String,String> getKakaoLoginResponse(KakaoMemberVO user){
//		
//		Map<String, String> loginResponse = new HashMap<>();
//		
//		loginResponse.put("userId",user.getMemberId());
//		loginResponse.put("userNo", String.valueOf(user.getUserNo()));
//		loginResponse.put("birthDay", user.getBirthDay());
//		loginResponse.put("userName", user.getMemberName());
//		loginResponse.put("email", user.getEmail());
//		loginResponse.put("phone", user.getPhone());
//		loginResponse.put("provider", user.getProvider());
//		loginResponse.put("role", user.getRole().toString());
//		
//		
//		return loginResponse;
//		
//	}

	private KakaoMemberDTO generateFileName(KakaoMemberDTO member, MultipartFile licenseImg) {

		if (licenseImg != null && !licenseImg.isEmpty()) {

			String filePath = fileService.store(licenseImg);

			member.setLicenseUrl(filePath);

			return member;
		}
		return null;
	}

	public void changePassword(ChangePasswordDTO password, CustomUserDetails user) {
		// 로그인한 유저 비밀번호 검증
		if (!passwordEncoder.matches(password.getUserPwd(), user.getPassword())) {
			throw new CustomAuthenticationException("비밀번호가 일치하지 않습니다.");
		}
		// 새 비밀번호 암호화
		String newPassword = passwordEncoder.encode(password.getChangePwd());

		// Mapper에 전달할 값
		Map<String, Object> changeRequest = Map.of("userNo", user.getUserNo(), "newPassword", newPassword);

		// Mapper 호출
		mapper.changePassword(changeRequest);

	}

	@Transactional
	public MemberDTO updateUser(MemberUpdateDTO member, MultipartFile licenseImg, CustomUserDetails userDetails) {
		// 1. 파일 처리
		String fileUrl = handleFileUpload(licenseImg);

		// 2. 변경된 필드만 추출
		Map<String, Object> changes = getChangedFields(member, userDetails, fileUrl);

		// 3. 업데이트 실행
		if (changes.size() > 1) { // userNo 외 변경필드 존재 시
			mapper.updateUser(changes);
		}

		// 4. 최신 회원 정보 반환
		return mapper.loadUser(userDetails.getUsername());
	}

	// 변경된 필드 추출
	private Map<String, Object> getChangedFields(MemberUpdateDTO dto, CustomUserDetails userDetails, String fileUrl) {
		Map<String, Object> changes = new HashMap<>();
		changes.put("userNo", userDetails.getUserNo());

		if (fileUrl != null) {
			changes.put("licenseUrl", fileUrl);
		}
		if (!dto.getMemberName().equals(userDetails.getRealName())) {
			changes.put("memberName", dto.getMemberName());
		}
		if (!dto.getEmail().equals(userDetails.getEmail())) {
			changes.put("email", dto.getEmail());
		}
		if (!dto.getPhone().equals(userDetails.getPhone())) {
			changes.put("phone", dto.getPhone());
		}

		return changes;
	}

	private String handleFileUpload(MultipartFile licenseImg) {
	    if (licenseImg != null && !licenseImg.isEmpty()) {
	        try {
	            return fileService.store(licenseImg);
	        } catch (Exception e) {
	            throw new FileUploadException("파일 업로드에 실패했습니다.");
	        }
	    }
	    return null;
	}
	// 원래 사용했으나 현재 로그인한 유저로 인증하고싶어서 삭제
	/*
	 * private CustomUserDetails validatePassword(String password) {
	 * 
	 * // 사용자가 입력한 비밀번호가 DB에 저장된 비밀번호 암호문이 쿵짜작 이게 맞는지 검증 Authentication auth =
	 * SecurityContextHolder.getContext().getAuthentication(); CustomUserDetails
	 * user = (CustomUserDetails) auth.getPrincipal(); // 검증이 맞다면 if
	 * (!passwordEncoder.matches(password, user.getPassword())) { throw new
	 * CustomAuthenticationException("비밀번호가 일치하지 않습니다."); }
	 * 
	 * return user;
	 * 
	 * }
	 */

	@Override
	@Transactional
	public void deleteByPassword(String inputPassword, CustomUserDetails user) {

		// 로그인한 유저 비밀번호 검증
		if (!passwordEncoder.matches(inputPassword, user.getPassword())) {
			throw new CustomAuthenticationException("비밀번호가 일치하지 않습니다.");
		}
		// 토큰삭제
		tokenMapper.deleteTokenByUserNo(user.getUserNo());
		// 유저 삭제
		mapper.deleteUserNo(String.valueOf(user.getUserNo()));

	}

}
