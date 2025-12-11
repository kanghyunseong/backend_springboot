package com.kh.pcar.back.boards.notice.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.kh.pcar.back.boards.notice.model.dto.NoticeDTO;

@Mapper
public interface NoticeMapper {

	// 페이징 목록
    List<NoticeDTO> findAll(RowBounds rb);

    // 전체 개수
    long countNotices();
    
    // 검색 목록
    List<NoticeDTO> searchNotices(Map<String, Object> params);

    // 검색 총 개수
    long countSearchNotices(Map<String, Object> params);

    // 상세
    NoticeDTO findById(Long noticeNo);

    // 조회수 증가
    int increaseCount(Long noticeNo);
}