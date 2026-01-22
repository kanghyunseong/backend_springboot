package com.kh.pcar.back.boards.comment.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.pcar.back.auth.model.vo.CustomUserDetails;
import com.kh.pcar.back.boards.Report.dto.ReportRequestDTO;
import com.kh.pcar.back.boards.comment.model.dto.CommentDTO;
import com.kh.pcar.back.boards.comment.model.service.CommentService;
import com.kh.pcar.back.common.ApiResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    
    // 댓글 작성
    @PostMapping
    public ResponseEntity<ApiResponse<CommentDTO>> save(
            @RequestBody CommentDTO comment,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
    	
    	// 로그인 체크
    	if (userDetails == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("로그인이 필요합니다."));
        }
    	
        CommentDTO c = commentService.save(comment, userDetails);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("댓글이 등록되었습니다.", c));
    }

    // 특정 게시글의 댓글 전체 조회
    @GetMapping
    public ResponseEntity<List<CommentDTO>> findAll(
            @RequestParam(name = "boardNo") Long boardNo) {
        return ResponseEntity.ok(commentService.findAll(boardNo));
    }

    // 댓글 수정
    @PutMapping("/{commentNo}")
    public ResponseEntity<ApiResponse<Void>> update(
            @PathVariable(name = "commentNo") Long commentNo,
            @RequestBody CommentDTO comment,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

    	// 로그인 체크
        if (userDetails == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("로그인이 필요합니다."));
        }

        Long loginUserNo = userDetails.getUserNo();  // userNo 기준으로 권한 체크
        commentService.update(commentNo, comment.getCommentContent(), loginUserNo);

        return ResponseEntity
                .ok(ApiResponse.success("댓글이 수정되었습니다."));
    }

    // 댓글 삭제
    @DeleteMapping("/{commentNo}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable(name = "commentNo") Long commentNo,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

    	// 로그인 체크
        if (userDetails == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("로그인이 필요합니다."));
        }

        Long loginUserNo = userDetails.getUserNo();  // userNo 기준으로 권한 체크
        commentService.delete(commentNo, loginUserNo);

        return ResponseEntity
                .ok(ApiResponse.success("댓글이 삭제되었습니다."));
    }

    // 댓글 신고
    @PostMapping("/{commentNo}/report")
    public ResponseEntity<ApiResponse<Void>> report(
            @PathVariable(name = "commentNo") Long commentNo,
            @RequestBody ReportRequestDTO request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
    	
    	 // 로그인 체크
        if (userDetails == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("로그인이 필요합니다."));
        }
        
        Long reporterNo = userDetails.getUserNo();  // 로그인한 사람 USER_NO
        String reason = request.getReason();

        commentService.report(commentNo, reporterNo, reason);

        return ResponseEntity
                .ok(ApiResponse.success("신고가 접수되었습니다."));
    }
}
