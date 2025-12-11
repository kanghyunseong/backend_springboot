package com.kh.pcar.back.auth.model.service;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.kh.pcar.back.auth.model.vo.CustomUserDetails;
import com.kh.pcar.back.exception.UsernameNotFoundException;
import com.kh.pcar.back.member.model.dao.MemberMapper;
import com.kh.pcar.back.member.model.dto.MemberDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
	// AuthenticationManger가 실질적으로 사용자의 정보를 조회할 때 메소드를 호출하는 클래스

	private final MemberMapper mapper;

	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

		// log.info("userId : {}" , userId);

		MemberDTO user = mapper.loadUser(userId);

		if (user == null) {
			throw new UsernameNotFoundException("유저 결과가 없습니다.");
		}
		return CustomUserDetails.builder().userNo(user.getUserNo()).username(user.getMemberId())
				.password(user.getMemberPwd()).realName(user.getMemberName()).birthDay(user.getBirthDay())
				.email(user.getEmail()).phone(user.getPhone()).licenseUrl(user.getLicenseUrl())
				.authorities(Collections.singletonList(new SimpleGrantedAuthority(user.getRole()))).build();

	}

}