package com.kh.pcar.back.boards.notice.model.service;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.pcar.back.boards.PageResponseDTO;
import com.kh.pcar.back.boards.notice.model.dao.NoticeMapper;
import com.kh.pcar.back.boards.notice.model.dto.NoticeDTO;
import com.kh.pcar.back.exception.NoticeNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeMapper noticeMapper;
    private final int pageSize = 10; // 한 페이지에 10개

    @Override
    public PageResponseDTO<NoticeDTO> NoticeList(int pageNo) {
    	if (pageNo < 0) {
    		throw new InvalidParameterException("페이지 번호가 올바르지 않습니다.");
    	}
    	
        int offset = pageNo * pageSize;
        RowBounds rb = new RowBounds(offset, pageSize);
        
        List<NoticeDTO> list = noticeMapper.findAll(rb);
        long total = noticeMapper.countNotices();
        int totalPages = (int) Math.ceil(total / (double) pageSize);

        return new PageResponseDTO<>(
                list,
                totalPages,
                total,
                pageNo,
                pageSize
        );
    }

    @Override
    public PageResponseDTO<NoticeDTO> searchNotices(String type, String keyword, int pageNo) {
    	if (pageNo < 0) {
    		throw new InvalidParameterException("페이지 번호가 올바르지 않습니다.");
    	}
    	
        int offset = pageNo * pageSize;

        Map<String, Object> params = new HashMap<>();
        params.put("type", type);
        params.put("keyword", "%" + keyword + "%");
        params.put("offset", offset);
        params.put("pageSize", pageSize);
        
        List<NoticeDTO> list = noticeMapper.searchNotices(params);
        long total = noticeMapper.countSearchNotices(params);
        int totalPages = (int) Math.ceil(total / (double) pageSize);

        return new PageResponseDTO<>(
                list,
                totalPages,
                total,
                pageNo,
                pageSize
        );
    }

    @Override
    @Transactional
    public NoticeDTO NoticeDetail(Long noticeNo) {
        // 조회수 증가
        noticeMapper.increaseCount(noticeNo);

        // 상세 조회
        NoticeDTO notice = noticeMapper.findById(noticeNo);
        if (notice == null) {
            throw new NoticeNotFoundException("해당 공지사항이 존재하지 않습니다.");
        }
        return notice;
    }
}
