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
	private final Pagenation pagenation; // Pagenation 빈 주입
	
	@Override
	@Transactional(readOnly = true)
	public UserPageResponseDTO findAllMember(int currentPage) {
		
        // 1. 전체 유저 수(listCount) 조회
		int totalCount = userMapper.getTotalCount();
        
        // 2. 페이징 설정값 정의 (한 페이지의 게시물 수, 페이지 버튼 수)
        int boardLimit = 10; // 한 페이지에 10개씩
        int pageLimit = 5;   // 페이지 버튼 5개씩
        
        // 3. Pagenation 유틸리티로 PageInfo 객체 생성
		PageInfo pi = pagenation.getPageInfo(
                            totalCount, 
                            currentPage, 
                            boardLimit, 
                            pageLimit
                        );
		
        // 4. RowBounds 생성 (몇 개 건너뛰고(offset), 몇 개 가져와라(limit))
        int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
        RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
        
        // 5. Mapper 호출 (RowBounds 전달)
		List<UserDTO> users = userMapper.findAllMember(rowBounds);
		
        // 6. PageInfo 객체와 List<UserDTO>를 하나의 응답 DTO로 묶어서 반환
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
		userMapper.updateUser(userDTO);
		
		return userMapper.findByUserNo(userDTO.getUserNo());
		
	}

	@Override
	@Transactional(readOnly = true)
	public UserDTO findUserByNo(Long userNo) {
		return userMapper.findByUserNo(userNo);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Map<String, Object>> getJoinTrend(String unit) {
		// TODO Auto-generated method stub
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