package com.kh.pcar.back.auth.model.service;
import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.kh.pcar.back.auth.model.vo.CustomUserDetails;
import com.kh.pcar.back.exception.UsernameNotFoundException;
import com.kh.pcar.back.member.dao.MemberMapper;
import com.kh.pcar.back.member.dto.MemberDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
	// AuthenticationManger가 실질적으로 사용자의 정보를 조회할 때 메소드를 호출하는 클래스
	
	private final MemberMapper mapper;
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		
		MemberDTO user = mapper.loadUser(username);
		
		log.info("이거 오나요 : {}", user);
		
		if(user == null) {
			throw new UsernameNotFoundException("그럼 죽어!!");
		}
		
		
		
		return CustomUserDetails.builder().username(user.getMemberId())
										  .password(user.getMemberPwd())
										  .memberName(user.getMemberName())
										  .authorities(Collections.singletonList(new SimpleGrantedAuthority(user.getRole())))
										  .build();
										  
	}
	
}
