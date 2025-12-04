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
		
		//log.info("Member에 들어온 값 {} , Multipart : {}" , member,licenseImg);
		
		memberService.join(member , licenseImg);
		
		return ResponseEntity.status(201).build();
		
		
	}
	
	
	@PostMapping("/kakao")
	public ResponseEntity<Map<String,String>> kakaoJoin(@Valid @ModelAttribute KakaoMemberDTO member , @RequestParam(name="licenseImg" , required = false ) MultipartFile licenseImg ){
		
		//log.info("Member에 들어온 값 {} , Multipart : {} " , member , licenseImg);
		
		Map<String,String> loginResponse = memberService.kakaoJoin(member,licenseImg);
		
		return ResponseEntity.ok(loginResponse);
	}
	
	@PutMapping
	public ResponseEntity<?> updatePassword(@Valid @RequestBody ChangePasswordDTO password
			, @AuthenticationPrincipal CustomUserDetails userDetails){
		
		//log.info(" password : {}" , password);
		
		memberService.changePassword(password,userDetails);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@PutMapping("/updateUser")
	public ResponseEntity<MemberDTO> updateUser(@Valid @ModelAttribute MemberUpdateDTO member ,  @RequestParam(name="licenseImg" , required = false ) MultipartFile licenseImg ,
										@AuthenticationPrincipal CustomUserDetails userDetails){
		
	//	log.info("member : {} , file : {}" , member ,licenseImg);
	  //  log.info("user : {} " , userDetails );
		
		  MemberDTO updatedMember = memberService.updateUser(member, licenseImg, userDetails);
		
		  log.info(" update : {} " , updatedMember);
		  
		  return ResponseEntity.ok(updatedMember);
	}
	
	@DeleteMapping
	public ResponseEntity<?> deleteByPassword(@RequestBody Map<String,String> request
			, @AuthenticationPrincipal CustomUserDetails userDetails
			){
		
		//log.info("이게 오나? {} ", request);
		
		memberService.deleteByPassword(request.get("userPwd"),userDetails);
		
		
		
		return ResponseEntity.ok("회원 탈퇴 완료");
	}
	

	
}
