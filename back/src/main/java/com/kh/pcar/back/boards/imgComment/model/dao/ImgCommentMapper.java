package com.kh.pcar.back.boards.imgComment.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kh.pcar.back.boards.imgComment.model.dto.ImgCommentDTO;
import com.kh.pcar.back.boards.imgComment.model.vo.ImgCommentVO;

@Mapper
public interface ImgCommentMapper {
	
int save(ImgCommentVO comment);
	
	List<ImgCommentDTO> findAll(Long boardNo);
}
