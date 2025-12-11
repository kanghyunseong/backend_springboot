package com.kh.pcar.back.boards.notice.model.service;

import com.kh.pcar.back.boards.PageResponseDTO;
import com.kh.pcar.back.boards.notice.model.dto.NoticeDTO;

public interface NoticeService {

    // 전체 공지 목록
	PageResponseDTO<NoticeDTO> NoticeList(int pageNo);
	 
	// 검색
	PageResponseDTO<NoticeDTO> searchNotices(String type, String keyword, int pageNo);
	
    // 공지 상세 (조회수 증가 포함)
    NoticeDTO NoticeDetail(Long noticeNo);
}