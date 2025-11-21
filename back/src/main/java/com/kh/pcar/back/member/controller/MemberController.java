package com.kh.pcar.back.member.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kh.pcar.back.auth.model.dto.KakaoProfileDTO;
import com.kh.pcar.back.member.model.dto.KakaoMemberDTO;
import com.kh.pcar.back.member.model.dto.MemberDTO;
import com.kh.pcar.back.member.model.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("members")
@RequiredArgsConstructor
public class MemberController {

	
	private final MemberService memberService;
	// 회원가입 => 일반회원 => ROLE 컬럼에 들어갈 값 필드에 담아주어야함
	//                  => 비밀번호 암호화
	//                  => VO에 담을것
	// VO : ValueObject(값을 담는 역할) ==> 불변해야한다는것이 특징
	// DTO : DataTransferObject(데이터 전송)
	
	// GET
	// GET(/members/멤버번)
	// POST
	// PUT
	// DELETE
	
	@PostMapping
	public ResponseEntity<?> join(@Valid @ModelAttribute MemberDTO member , @RequestParam(name="licenseImg" , required = false ) MultipartFile licenseImg){
		
		log.info("Member에 들어온 값 {} , Multipart : {}" , member,licenseImg);
		
		memberService.join(member , licenseImg);
		
		return ResponseEntity.status(201).build();
		
		
	}
	
	
	@PostMapping("/kakao")
	public ResponseEntity<?> kakaoJoin(@Valid @ModelAttribute KakaoMemberDTO member , @RequestParam(name="licenseImg" , required = false ) MultipartFile licenseImg ){
		
		log.info("Member에 들어온 값 {} , Multipart : {} " , member , licenseImg);
		
		Map<String,String> loginResponse = memberService.kakaoJoin(member,licenseImg);
		
		return ResponseEntity.ok(loginResponse);
	}
	

	
}
