package com.kh.pcar.back.boards.imgComment.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kh.pcar.back.auth.model.vo.CustomUserDetails;
import com.kh.pcar.back.boards.imgBoard.model.service.ImgBoardService;
import com.kh.pcar.back.boards.imgComment.model.dao.ImgCommentMapper;
import com.kh.pcar.back.boards.imgComment.model.dto.ImgCommentDTO;
import com.kh.pcar.back.boards.imgComment.model.vo.ImgCommentVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImgCommentServiceImpl implements ImgCommentService {

	private final ImgBoardService imgBoardService;
	private final ImgCommentMapper imgCommentMapper;
	
	@Override
	public ImgCommentDTO save(ImgCommentDTO imgComment, CustomUserDetails userDetails) {
		
		imgBoardService.findByImgBoardNo(imgComment.getRefIno()); // 외부에 노출된 메소드 호
		String memberId = userDetails.getUsername();
		
		ImgCommentDTO ic = ImgCommentDTO.builder()
							  .imgCommentWriter(memberId)
							  .imgCommentContent(imgComment.getImgCommentContent())
							  .refIno(imgComment.getRefIno())
							  .build();
		imgCommentMapper.save(ic);
		return ic;
	}

	@Override
	   public List<ImgCommentDTO> findAll(Long boardNo) {
	       imgBoardService.findByImgBoardNo(boardNo);
	       return imgCommentMapper.findAll(boardNo);
	}

    @Override
    public void update(Long imgCommentNo, String imgCommentContent, String loginId) {
        ImgCommentDTO imgComment = new ImgCommentDTO();
        imgComment.setImgCommentNo(imgCommentNo);
        imgComment.setImgCommentContent(imgCommentContent);

        int result = imgCommentMapper.update(imgComment);
        if (result <= 0) {
            throw new RuntimeException("갤러리 댓글 수정에 실패했습니다.");
        }
    }

    @Override
    public void delete(Long imgCommentNo, String loginId) {
        int result = imgCommentMapper.delete(imgCommentNo);
        if (result <= 0) {
            throw new RuntimeException("갤러리 댓글 삭제에 실패했습니다.");
        }
    }

    @Override
    public void report(Long imgCommentNo, String loginId, String reason) {
        int result = imgCommentMapper.reportRequest(imgCommentNo, loginId, reason);
        if (result <= 0) {
            throw new RuntimeException("갤러리 댓글 신고에 실패했습니다.");
        }
    }
}
