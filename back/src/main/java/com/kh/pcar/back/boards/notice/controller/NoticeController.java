package com.kh.pcar.back.boards.notice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.pcar.back.boards.PageResponseDTO;
import com.kh.pcar.back.boards.notice.model.dto.NoticeDTO;
import com.kh.pcar.back.boards.notice.model.service.NoticeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/boards/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    // 전체 목록 조회
    @GetMapping
    public PageResponseDTO<NoticeDTO> getAllNotices(
            @RequestParam(name = "page", defaultValue = "0") int page) {
        return noticeService.getNoticeList(page);
    }
    
    // 목록 검색
    @GetMapping("/search")
    public PageResponseDTO<NoticeDTO> searchNotices(
            @RequestParam(name = "type") String type,
            @RequestParam(name = "keyword") String keyword,
            @RequestParam(name = "page", defaultValue = "0") int pageNo) {

        return noticeService.searchNotices(type, keyword, pageNo);
    }

    // 상세 조회 (조회수 증가 포함)
    @GetMapping("/{noticeNo}")
    public NoticeDTO getNoticeDetail(@PathVariable(name="noticeNo")  Long noticeNo) {
        return noticeService.getNoticeDetail(noticeNo);
    }
}
