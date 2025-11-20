package com.kh.pcar.back.boards.imgComment.model.service;

import java.util.List;

import com.kh.pcar.back.auth.model.vo.CustomUserDetails;
import com.kh.pcar.back.boards.imgComment.model.dto.ImgCommentDTO;
import com.kh.pcar.back.boards.imgComment.model.vo.ImgCommentVO;

public interface ImgCommentService {
	
	// 인서트 하나
	ImgCommentVO save(ImgCommentDTO comment, CustomUserDetails userDetails);
	
	// 조회 하나
	List<ImgCommentDTO> findAll(Long boardNo);
}
