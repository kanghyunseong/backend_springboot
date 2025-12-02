package com.kh.pcar.back.boards.notice.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kh.pcar.back.boards.notice.model.dto.NoticeDTO;

@Mapper
public interface NoticeMapper {

    // 전체 목록
    List<NoticeDTO> findAll();

    // 상세
    NoticeDTO findById(Long noticeNo);

    // 조회수 증가
    int increaseCount(Long noticeNo);
}