package com.kh.pcar.back.boards.comment.model.service;

import java.util.List;

import com.kh.pcar.back.auth.model.vo.CustomUserDetails;
import com.kh.pcar.back.boards.comment.model.dto.CommentDTO;

public interface CommentService {
	
	// 댓글 작성
	CommentDTO save(CommentDTO comment, CustomUserDetails userDetails);

    // 특정 게시글의 댓글 전체 조회
    List<CommentDTO> findAll(Long boardNo);

    // 댓글 수정
    void update(Long commentNo, String commentContent, Long loginUserNo);

    // 댓글 삭제(STATUS 변경)
    void delete(Long commentNo, Long loginUserNo);

    // 댓글 신고
    void report(Long commentNo, Long reporterNo, String reason);
}
