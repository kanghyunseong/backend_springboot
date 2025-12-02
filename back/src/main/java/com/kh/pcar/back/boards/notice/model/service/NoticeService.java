package com.kh.pcar.back.boards.notice.model.service;

import java.util.List;

import com.kh.pcar.back.boards.notice.model.dto.NoticeDTO;

public interface NoticeService {

    // 전체 공지 목록
    List<NoticeDTO> getNoticeList();

    // 공지 상세 (조회수 증가 포함)
    NoticeDTO getNoticeDetail(Long noticeNo);
}