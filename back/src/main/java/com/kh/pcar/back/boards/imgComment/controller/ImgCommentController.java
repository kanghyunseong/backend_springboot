package com.kh.pcar.back.boards.imgComment.controller;

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
import com.kh.pcar.back.boards.imgComment.model.dto.ImgCommentDTO;
import com.kh.pcar.back.boards.imgComment.model.service.ImgCommentService;
import com.kh.pcar.back.common.ApiResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/imgComments")
@RequiredArgsConstructor
public class ImgCommentController {

    private final ImgCommentService imgCommentService;

    // 댓글 등록
    @PostMapping
    public ResponseEntity<ApiResponse<ImgCommentDTO>> save(
            @RequestBody ImgCommentDTO imgComment,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        // 로그인 체크
        if (userDetails == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("로그인이 필요합니다."));
        }

        ImgCommentDTO ic = imgCommentService.save(imgComment, userDetails);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("댓글이 등록되었습니다.", ic));
    }

    // 특정 갤러리 게시글의 댓글 전체 조회
    @GetMapping
    public ResponseEntity<List<ImgCommentDTO>> findAll(
            @RequestParam(name = "imgBoardNo") Long imgBoardNo) {
        return ResponseEntity.ok(imgCommentService.findAll(imgBoardNo));
    }

    // 댓글 수정
    @PutMapping("/{imgCommentNo}")
    public ResponseEntity<ApiResponse<Void>> update(
            @PathVariable("imgCommentNo") Long imgCommentNo,
            @RequestBody ImgCommentDTO imgComment,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        // 로그인 체크
        if (userDetails == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("로그인이 필요합니다."));
        }

        Long loginUserNo = userDetails.getUserNo(); // USER_NO 기준
        imgCommentService.update(imgCommentNo, imgComment.getImgCommentContent(), loginUserNo);

        return ResponseEntity
                .ok(ApiResponse.success("댓글이 수정되었습니다."));
    }

    // 댓글 삭제
    @DeleteMapping("/{imgCommentNo}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable("imgCommentNo") Long imgCommentNo,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        // 로그인 체크
        if (userDetails == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("로그인이 필요합니다."));
        }

        Long loginUserNo = userDetails.getUserNo(); // USER_NO 기준
        imgCommentService.delete(imgCommentNo, loginUserNo);

        return ResponseEntity
                .ok(ApiResponse.success("댓글이 삭제되었습니다."));
    }

    // 댓글 신고
    @PostMapping("/{imgCommentNo}/report")
    public ResponseEntity<ApiResponse<Void>> report(
            @PathVariable("imgCommentNo") Long imgCommentNo,
            @RequestBody ReportRequestDTO request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        // 로그인 체크
        if (userDetails == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("로그인이 필요합니다."));
        }

        Long reporterNo = userDetails.getUserNo();
        String reason = request.getReason();

        imgCommentService.report(imgCommentNo, reporterNo, reason);

        return ResponseEntity
                .ok(ApiResponse.success("신고가 접수되었습니다."));
    }
}
