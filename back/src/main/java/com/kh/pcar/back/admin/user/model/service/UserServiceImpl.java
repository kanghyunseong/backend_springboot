package com.kh.pcar.back.admin.user.model.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.pcar.back.admin.user.model.dao.UserMapper;
import com.kh.pcar.back.admin.user.model.dto.UserDTO;
import com.kh.pcar.back.admin.user.model.dto.UserKpiStatsDTO;
import com.kh.pcar.back.admin.user.model.dto.UserPageResponseDTO;
import com.kh.pcar.back.exception.MemberJoinException;
import com.kh.pcar.back.exception.UserNotFoundException;
import com.kh.pcar.back.util.PageInfo;
import com.kh.pcar.back.util.Pagenation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // SLF4J 로거 import

@Service
@RequiredArgsConstructor
@Slf4j // 로거 사용을 위한 Lombok 어노테이션
public class UserServiceImpl implements UserService {

	private final UserMapper userMapper;
	private final Pagenation pagenation;
	
	@Override
	@Transactional(readOnly = true)
	public UserPageResponseDTO findAllMember(int currentPage) {
		
		int totalCount = userMapper.getTotalCount();
		
		int boardLimit = 10;
		int pageLimit = 5;
		
		PageInfo pi = pagenation.getPageInfo(
		            totalCount, 
		            currentPage, 
		            boardLimit, 
		            pageLimit
		        );
		
		int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		
		List<UserDTO> users = userMapper.findAllMember(rowBounds);
		
		return new UserPageResponseDTO(pi, users);
	}

	@Override
	@Transactional
	public void deleteUser(Long userNo) {
		int result = userMapper.deleteUserStatus(userNo);
		
		if(result == 0) {
			// 비즈니스 로직에 따른 예외 발생 (트랜잭션 롤백)
			log.warn("User delete failed. UserNo {} not found or already deleted.", userNo);
			throw new UserNotFoundException("사용자 번호 " + userNo + "를 찾을 수 없거나 이미 삭제된 사용자입니다.");
		}
		
		
	}

	@Override
	@Transactional
	public UserDTO updateUser(UserDTO userDTO) {
		int result = userMapper.updateUser(userDTO);
		
		if(result == 0) {
			// 비즈니스 로직에 따른 예외 발생 (트랜잭션 롤백)
			log.warn("User update failed. UserNo {} not found.", userDTO.getUserNo());
			throw new UserNotFoundException("수정할 사용자(No: " + userDTO.getUserNo() + ")를 찾을 수 없습니다.");
		}
		
		return userMapper.findByUserNo(userDTO.getUserNo());
	}

	@Override
	@Transactional(readOnly = true)
	public UserDTO findUserByNo(Long userNo) {
		UserDTO user = userMapper.findByUserNo(userNo);
		
		if(user == null) {
			// 비즈니스 로직에 따른 예외 발생
			log.warn("User lookup failed. UserNo {} not found.", userNo);
			throw new UserNotFoundException("사용자 번호 " + userNo + "에 대한 정보가 없습니다.");
		}
		
		return user;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Map<String, Object>> getJoinTrend(String unit) {
		
		List<Map<String, Object>> result;
		try {
			result = userMapper.getJoinTrend(unit);
		} catch (Exception e) {
			log.error("Failed to retrieve join trend data for unit: {}", unit, e);
			throw new MemberJoinException("회원가입 트렌드 정보를 조회하는 중 데이터베이스 오류가 발생했습니다.");
		}

		if (result == null || result.isEmpty()) {
			log.warn("Member join trend data not found for unit: {}", unit);
			throw new MemberJoinException("회원가입한 유저들의 정보를 찾을 수 없음");
		}
		
		return result; 
	}

	@Override
	@Transactional(readOnly = true)
	public UserKpiStatsDTO getKpiStats() {
		int totalActiveUsers = userMapper.getTotalCount();
		int waitingLicenseCount = userMapper.getWaitingLicenseCount();
		
		return new UserKpiStatsDTO(totalActiveUsers, waitingLicenseCount);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Map<String, Object>> getLicenseStatusTrend(String unit) {
		return userMapper.getLicenseStatusTrend(unit);
	}
}