package com.kh.pcar.back.admin.notice.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// import com.kh.pcar.back.admin.cars.model.service.FileSaveService; // 사용 안 하면 제거
import com.kh.pcar.back.admin.notice.model.dao.AdminNoticeMapper;
import com.kh.pcar.back.admin.notice.model.dto.AdminNoticeDTO;
import com.kh.pcar.back.exception.NoticeNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminNoticeServiceImpl implements AdminNoticeService {

	private final AdminNoticeMapper adminNoticeMapper;

	@Override
	@Transactional(readOnly = true) 
	public List<AdminNoticeDTO> findAllNotice() {
		return adminNoticeMapper.findAllNotice();
	}

	@Override
	@Transactional
	public void deleteNotice(Long noticeNo) {
		if (noticeNo == null || noticeNo <= 0) {
			throw new IllegalArgumentException("유효하지 않은 공지사항 번호입니다.");
		}

		int result = adminNoticeMapper.deleteNotice(noticeNo);
		
		if (result == 0) {
			throw new NoticeNotFoundException("공지사항 번호 " + noticeNo + "를 찾을 수 없습니다.");
		}
	}

	@Override
	@Transactional(readOnly = true) // [추가] 조회 성능 최적화
	public AdminNoticeDTO findNoticeByNo(Long noticeNo) {
		AdminNoticeDTO adminNoticeDTO = adminNoticeMapper.findNoticeByNo(noticeNo);
		
		if (adminNoticeDTO == null) {
			throw new NoticeNotFoundException("공지사항 번호 " + noticeNo + "에 대한 정보가 없습니다.");
		}
		return adminNoticeDTO;
	}

	@Override
	@Transactional
	public void registerNotice(AdminNoticeDTO adminNoticeDTO) {
		if (adminNoticeDTO.getNoticeTitle() == null || adminNoticeDTO.getNoticeTitle().trim().isEmpty()) {
			throw new IllegalArgumentException("공지사항 제목은 필수입니다.");
		}
		adminNoticeMapper.insertNotice(adminNoticeDTO);
	}
	
	@Override
	@Transactional
	public void modifyNotice(AdminNoticeDTO adminNoticeDTO) {
		int result = adminNoticeMapper.updateNotice(adminNoticeDTO);
		if (result == 0) {
			throw new NoticeNotFoundException("공지사항 번호 " + adminNoticeDTO.getNoticeNo() + "번을 찾을 수 없습니다.");
		}
	}

}