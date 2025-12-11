package com.kh.pcar.back.boards.imgBoard.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kh.pcar.back.boards.imgBoard.model.vo.AttachmentVO;

@Mapper
public interface AttachmentMapper {
    void insertAttachment(AttachmentVO vo);
    List<AttachmentVO> findByRefIno(Long refIno);
    
    // 기존 첨부 전부 비활성화
    void disableByRefIno(Long refIno);
}
