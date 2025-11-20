package com.kh.pcar.back.boards.imgComment.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kh.pcar.back.auth.model.vo.CustomUserDetails;
import com.kh.pcar.back.boards.imgBoard.model.service.ImgBoardService;
import com.kh.pcar.back.boards.imgComment.model.dao.ImgCommentMapper;
import com.kh.pcar.back.boards.imgComment.model.dto.ImgCommentDTO;
import com.kh.pcar.back.boards.imgComment.model.vo.ImgCommentVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImgCommentServiceImpl implements ImgCommentService {

	private final ImgBoardService imgBoardService;
	private final ImgCommentMapper imgCommentMapper;
	
	@Override
	public ImgCommentVO save(ImgCommentDTO comment, CustomUserDetails userDetails) {
		
		imgBoardService.findByImgBoardNo(comment.getRefIno()); // 외부에 노출된 메소드 호
		String memberId = userDetails.getUsername();
		
		ImgCommentVO ic =ImgCommentVO.builder()
							  .imgCommentWriter(memberId)
							  .imgCommentContent(comment.getImgCommentContent())
							  .refIno(comment.getRefIno())
							  .build();
		imgCommentMapper.save(ic);
		return ic;
	}

	@Override
	public List<ImgCommentDTO> findAll(Long boardNo) {
		imgBoardService.findByImgBoardNo(boardNo);
		return imgCommentMapper.findAll(boardNo);
	}
}
