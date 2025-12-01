package com.kh.pcar.back.boards.notice.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.pcar.back.boards.notice.model.dao.NoticeMapper;
import com.kh.pcar.back.boards.notice.model.dto.NoticeDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeMapper noticeMapper;

    @Override
    public List<NoticeDTO> getNoticeList() {
        return noticeMapper.findAll();
    }

    @Override
    @Transactional
    public NoticeDTO getNoticeDetail(Long noticeNo) {
        // 조회수 증가
        noticeMapper.increaseCount(noticeNo);

        // 상세 조회
        NoticeDTO notice = noticeMapper.findById(noticeNo);
        if (notice == null) {
            throw new IllegalArgumentException("해당 공지사항이 존재하지 않습니다. noticeNo=" + noticeNo);
        }
        return notice;
    }
}
