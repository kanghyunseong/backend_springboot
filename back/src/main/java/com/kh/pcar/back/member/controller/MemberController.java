package com.kh.pcar.back.member.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kh.pcar.back.auth.model.vo.CustomUserDetails;
import com.kh.pcar.back.common.ResponseData;
import com.kh.pcar.back.member.model.dto.ChangePasswordDTO;
import com.kh.pcar.back.member.model.dto.KakaoMemberDTO;
import com.kh.pcar.back.member.model.dto.MemberDTO;
import com.kh.pcar.back.member.model.dto.MemberUpdateDTO;
import com.kh.pcar.back.member.model.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;
	// 회원가입 => 일반회원 => ROLE 컬럼에 들어갈 값 필드에 담아주어야함
	// => 비밀번호 암호화
	// => VO에 담을것
	// VO : ValueObject(값을 담는 역할) ==> 불변해야한다는것이 특징
	// DTO : DataTransferObject(데이터 전송)

	// GET
	// GET(/members/멤버번)
	// POST
	// PUT
	// DELETE

	@PostMapping
	public ResponseEntity<ResponseData<String>> join(@Valid @ModelAttribute MemberDTO member,
			@RequestParam(name = "licenseImg", required = false) MultipartFile licenseImg) {

		 log.info("Member에 들어온 값 {} , Multipart : {}" , member,licenseImg);

		memberService.join(member, licenseImg);

		return ResponseData.created("회원가입에 성공했습니다.");

	}

	@PostMapping("/kakao")
	public ResponseEntity<ResponseData<Object>> kakaoJoin(@Valid @ModelAttribute KakaoMemberDTO member,
			@RequestParam(name = "licenseImg", required = false) MultipartFile licenseImg) {

		log.info("Member에 들어온 값 {} , Multipart : {} ", member, licenseImg);

		memberService.kakaoJoin(member, licenseImg);

		return ResponseData.ok(null,"카카오로그인완료");
	}

	@PutMapping
	public ResponseEntity<ResponseData<String>> updatePassword(@Valid @RequestBody ChangePasswordDTO password,
			@AuthenticationPrincipal CustomUserDetails userDetails) {

		// log.info(" password : {}" , password);
		// commit용 주석
		memberService.changePassword(password, userDetails);

		return ResponseData.created("비밀번호 변경에 성공했습니다");
	}

	@PutMapping("/updateUser")
	public ResponseEntity<ResponseData<Object>> updateUser(@Valid @ModelAttribute MemberUpdateDTO member,
			@RequestParam(name = "licenseImg", required = false) MultipartFile licenseImg,
			@AuthenticationPrincipal CustomUserDetails userDetails) {

		// log.info("member : {} , file : {}" , member ,licenseImg);
		// log.info("user : {} " , userDetails );

	

		//log.info(" update : {} ", updatedMember);

		return ResponseData.ok(memberService.updateUser(member, licenseImg, userDetails),"회원 업데이트 완료");
				
	}

	@DeleteMapping
	public ResponseEntity<ResponseData<String>> deleteByPassword(@RequestBody Map<String, String> request,
			@AuthenticationPrincipal CustomUserDetails userDetails) {

		// log.info("이게 오나? {} ", request);

		memberService.deleteByPassword(request.get("userPwd"), userDetails);

		return ResponseData.ok(null,"회원 탈퇴 완료");
	}

}