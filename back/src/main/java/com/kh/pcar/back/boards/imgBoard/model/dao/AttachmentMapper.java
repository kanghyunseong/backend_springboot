package com.kh.pcar.back.boards.imgBoard.model.dao;

import java.util.List;

import com.kh.pcar.back.boards.imgBoard.model.vo.AttachmentVO;

public interface AttachmentMapper {
    void insertAttachment(AttachmentVO vo);
    List<AttachmentVO> findByRefIno(Long refIno);
}
