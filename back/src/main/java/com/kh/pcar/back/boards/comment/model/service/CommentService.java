package com.kh.pcar.back.boards.comment.model.service;

import java.util.List;

import com.kh.pcar.back.auth.model.vo.CustomUserDetails;
import com.kh.pcar.back.boards.comment.model.dto.CommentDTO;
import com.kh.pcar.back.boards.comment.model.vo.CommentVO;

public interface CommentService {
	
	// 인서트 하나
	CommentVO save(CommentDTO comment, CustomUserDetails userDetails);
	
	// 조회 하나
	List<CommentDTO> findAll(Long boardNo);
	
}
