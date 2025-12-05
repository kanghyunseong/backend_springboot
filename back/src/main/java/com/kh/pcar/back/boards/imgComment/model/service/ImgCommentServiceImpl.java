package com.kh.pcar.back.boards.imgComment.model.service;

import java.security.InvalidParameterException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kh.pcar.back.auth.model.vo.CustomUserDetails;
import com.kh.pcar.back.boards.Report.dto.ReportDTO;
import com.kh.pcar.back.boards.Report.service.ReportService;
import com.kh.pcar.back.boards.imgBoard.model.service.ImgBoardService;
import com.kh.pcar.back.boards.imgComment.model.dao.ImgCommentMapper;
import com.kh.pcar.back.boards.imgComment.model.dto.ImgCommentDTO;
import com.kh.pcar.back.exception.CustomAuthenticationException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImgCommentServiceImpl implements ImgCommentService {

	private final ImgBoardService imgBoardService;
	private final ImgCommentMapper imgCommentMapper;
	private final ReportService reportService; // 신고 기능 서비스 호출
	
	@Override
	public ImgCommentDTO save(ImgCommentDTO imgComment, CustomUserDetails userDetails) {
		
		// 1. 게시글 존재 여부 확인 (없으면 InvalidParameterException -> 400)
		imgBoardService.findByImgBoardNo(imgComment.getRefIno());

		// 2. 작성자 ID 세팅
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
	    // 게시글 존재 체크 (없으면 400)
	    imgBoardService.findByImgBoardNo(boardNo);
	    return imgCommentMapper.findAll(boardNo);
	}

    @Override
    public void update(Long imgCommentNo, String imgCommentContent, Long loginUserNo) {

        // 1. 댓글 작성자 USER_NO 조회
        Long writerUserNo = imgCommentMapper.findWriterUserNo(imgCommentNo);

        // 1-1. 존재하지 않는 댓글
        if (writerUserNo == null) {
            throw new InvalidParameterException("존재하지 않는 댓글입니다.");
        }

        // 1-2. 작성자가 아닌 경우
        if (!writerUserNo.equals(loginUserNo)) {
            throw new CustomAuthenticationException("작성자만 댓글을 수정할 수 있습니다.");
        }

        // 2. 실제 수정 처리
        ImgCommentDTO imgComment = new ImgCommentDTO();
        imgComment.setImgCommentNo(imgCommentNo);
        imgComment.setImgCommentContent(imgCommentContent);

        int result = imgCommentMapper.update(imgComment);
        if (result <= 0) {
            throw new RuntimeException("갤러리 댓글 수정에 실패했습니다.");
        }
    }

    @Override
    public void delete(Long imgCommentNo, Long loginUserNo) {

        // 1. 댓글 작성자 USER_NO 조회
        Long writerUserNo = imgCommentMapper.findWriterUserNo(imgCommentNo);

        // 1-1. 존재하지 않는 댓글
        if (writerUserNo == null) {
            throw new InvalidParameterException("존재하지 않는 댓글입니다.");
        }

        // 1-2. 작성자가 아닌 경우
        if (!writerUserNo.equals(loginUserNo)) {
            throw new CustomAuthenticationException("작성자만 댓글을 삭제할 수 있습니다.");
        }

        // 2. 실제 삭제 (STATUS = 'N')
        int result = imgCommentMapper.delete(imgCommentNo);
        if (result <= 0) {
            throw new RuntimeException("갤러리 댓글 삭제에 실패했습니다.");
        }
    }

    @Override
    public void report(Long imgCommentNo, Long reporterNo, String reason) {
    	// 1. 댓글이 존재하는지 검증
        Long reportedUserNo = imgCommentMapper.findWriterUserNo(imgCommentNo);
        if (reportedUserNo == null) {
            throw new InvalidParameterException("존재하지 않는 댓글입니다.");
        }

        ReportDTO reportDTO = ReportDTO.builder()
                .targetType("IMGCOMMENT")
                .targetNo(imgCommentNo)
                .reportedUser(reportedUserNo)
                .reason(reason)
                .build();
        
        // 통합 신고 서비스 호출
        reportService.report(reporterNo, reportDTO);
    }
}
