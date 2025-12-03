package com.kh.pcar.back.boards.imgComment.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kh.pcar.back.boards.imgComment.model.dto.ImgCommentDTO;

@Mapper
public interface ImgCommentMapper {

    // 댓글 등록
    int save(ImgCommentDTO iC);

    // 특정 갤러리 게시글 댓글 목록
    List<ImgCommentDTO> findAll(@Param("imgBoardNo") Long imgBoardNo);

    // 댓글 내용 수정
    int update(ImgCommentDTO imgComment);

    // 댓글 삭제(STATUS = 'N')
    int delete(@Param("imgCommentNo") Long imgCommentNo);

    // 댓글 신고용: 댓글 작성자 USER_NO 조회
    Long findWriterUserNo(@Param("imgCommentNo") Long imgCommentNo);

}