package com.kh.pcar.back.boards.comment.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kh.pcar.back.boards.comment.model.dto.CommentDTO;
import com.kh.pcar.back.boards.comment.model.vo.CommentVO;

@Mapper
public interface CommentMapper {
	
	// 댓글 등록
    int save(CommentDTO c);

    // 특정 게시글의 댓글 목록
    List<CommentDTO> findAll(@Param("boardNo") Long boardNo);

    // 댓글 내용 수정
    int update(CommentDTO comment);

    // 댓글 삭제(STATUS 변경)
    int delete(@Param("commentNo") Long commentNo);

    // 댓글 신고 등록
    int reportRequest(@Param("commentNo") Long commentNo,
                      @Param("reporterId") String reporterId,
                      @Param("reason") String reason);
}
