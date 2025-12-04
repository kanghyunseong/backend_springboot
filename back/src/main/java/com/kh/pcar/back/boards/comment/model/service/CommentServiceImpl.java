package com.kh.pcar.back.boards.comment.model.service;

import java.security.InvalidParameterException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kh.pcar.back.auth.model.vo.CustomUserDetails;
import com.kh.pcar.back.boards.Report.dto.ReportDTO;
import com.kh.pcar.back.boards.Report.service.ReportService;
import com.kh.pcar.back.boards.board.model.service.BoardService;
import com.kh.pcar.back.boards.comment.model.dao.CommentMapper;
import com.kh.pcar.back.boards.comment.model.dto.CommentDTO;
import com.kh.pcar.back.exception.CustomAuthenticationException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
	
	private final BoardService boardService;
    private final CommentMapper commentMapper;
    private final ReportService reportService; // 신고 기능 서비스 호출

    @Override
	public CommentDTO save(CommentDTO comment, CustomUserDetails userDetails) {
		
		boardService.findByBoardNo(comment.getRefBno()); // 외부에 노출된 메소드 호출
		String memberId = userDetails.getUsername();
		
		CommentDTO c = CommentDTO.builder()
							  .commentWriter(memberId)
							  .commentContent(comment.getCommentContent())
							  .refBno(comment.getRefBno())
							  .build();
		commentMapper.save(c);
		return c;
	}

	@Override
	public List<CommentDTO> findAll(Long boardNo) {
		boardService.findByBoardNo(boardNo);
		return commentMapper.findAll(boardNo);
	}

    @Override
    public void update(Long commentNo, String commentContent, Long loginUserNo) {
    	// 1. 댓글 작성자 USER_NO 조회
        Long writerUserNo = commentMapper.findWriterUserNo(commentNo);

        // 1-1. 존재하지 않는 댓글
        if (writerUserNo == null) {
            throw new InvalidParameterException("존재하지 않는 댓글입니다.");
        }

        // 1-2. 작성자가 아닌 경우
        if (!writerUserNo.equals(loginUserNo)) {
            throw new CustomAuthenticationException("작성자만 댓글을 수정할 수 있습니다.");
        }

        // 2. 실제 수정 처리
        CommentDTO comment = new CommentDTO();
        comment.setCommentNo(commentNo);
        comment.setCommentContent(commentContent);

        int result = commentMapper.update(comment);
        if (result <= 0) {
            throw new RuntimeException("댓글 수정에 실패했습니다.");
        }
    }

    @Override
    public void delete(Long commentNo, Long loginUserNo) {

        // 1. 댓글 작성자 USER_NO 조회
        Long writerUserNo = commentMapper.findWriterUserNo(commentNo);

        // 1-1. 존재하지 않는 댓글
        if (writerUserNo == null) {
            throw new InvalidParameterException("존재하지 않는 댓글입니다.");
        }

        // 1-2. 작성자가 아닌 경우
        if (!writerUserNo.equals(loginUserNo)) {
            throw new CustomAuthenticationException("작성자만 댓글을 삭제할 수 있습니다.");
        }

        // 2. 실제 삭제 (STATUS = 'N')
        int result = commentMapper.delete(commentNo);
        if (result <= 0) {
            throw new RuntimeException("댓글 삭제에 실패했습니다.");
        }
    }

    @Override
    public void report(Long commentNo, Long reporterNo, String reason) {
        // 1. 댓글이 존재하는지 간단히 검증
        // findWriterUserNo가 null 이면 없는 댓글로 보면 됨.

        Long reportedUserNo = commentMapper.findWriterUserNo(commentNo);
        if (reportedUserNo == null) {
            throw new IllegalArgumentException("존재하지 않는 댓글입니다.");
        }

        ReportDTO reportDTO = ReportDTO.builder()
                .targetType("COMMENT")
                .targetNo(commentNo)
                .reportedUser(reportedUserNo)
                .reason(reason)
                .build();
        
        // 통합 신고 서비스 호출
        reportService.report(reporterNo, reportDTO);
    }
}
