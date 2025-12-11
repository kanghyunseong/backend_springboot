package com.kh.pcar.back.admin.user.model.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.pcar.back.admin.user.model.dao.UserMapper;
import com.kh.pcar.back.admin.user.model.dto.UserDTO;
import com.kh.pcar.back.admin.user.model.dto.UserKpiStatsDTO;
import com.kh.pcar.back.admin.user.model.dto.UserPageResponseDTO;
import com.kh.pcar.back.exception.UserNotFoundException;
import com.kh.pcar.back.util.PageInfo;
import com.kh.pcar.back.util.Pagenation;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
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
			throw new UserNotFoundException("사용자 번호 " + userNo + "를 찾을 수 없거나 이미 삭제된 사용자입니다.");
		}
	}

	@Override
	@Transactional
	public UserDTO updateUser(UserDTO userDTO) {
		int result = userMapper.updateUser(userDTO);
		
		if(result == 0) {
			throw new UserNotFoundException("수정할 사용자(No: " + userDTO.getUserNo() + ")를 찾을 수 없습니다.");
		}
		
		return userMapper.findByUserNo(userDTO.getUserNo());
	}

	@Override
	@Transactional(readOnly = true)
	public UserDTO findUserByNo(Long userNo) {
		UserDTO user = userMapper.findByUserNo(userNo);
		
		if(user == null) {
			throw new UserNotFoundException("사용자 번호 " + userNo + "에 대한 정보가 없습니다.");
		}
		
		return user;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Map<String, Object>> getJoinTrend(String unit) {
		return userMapper.getJoinTrend(unit);
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