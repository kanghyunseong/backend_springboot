package com.kh.pcar.back.boards.imgComment.model.service;

import java.util.List;

import com.kh.pcar.back.auth.model.vo.CustomUserDetails;
import com.kh.pcar.back.boards.imgComment.model.dto.ImgCommentDTO;

public interface ImgCommentService {
	
	// 댓글 작성
	ImgCommentDTO save(ImgCommentDTO imgComment, CustomUserDetails userDetails);

	// 특정 게시글의 댓글 전체 조회
    List<ImgCommentDTO> findAll(Long imgBoardNo);

    // 댓글 수정 
    void update(Long imgCommentNo, String imgCommentContent, Long loginUserNo);

    // 댓글 삭제(STATUS 변경) 
    void delete(Long imgCommentNo, Long loginUserNo);

    // 댓글 신고
    void report(Long imgCommentNo, Long reporterNo, String reason);
}
