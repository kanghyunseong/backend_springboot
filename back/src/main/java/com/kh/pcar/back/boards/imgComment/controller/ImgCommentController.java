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
import com.kh.pcar.back.boards.imgComment.model.dto.ImgCommentDTO;
import com.kh.pcar.back.boards.imgComment.model.service.ImgCommentService;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/imgComments")
@RequiredArgsConstructor
public class ImgCommentController {

    private final ImgCommentService imgCommentService;

    // 댓글 등록
    @PostMapping
    public ResponseEntity<?> save(
            @RequestBody ImgCommentDTO imgComment,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

    	ImgCommentDTO ic = imgCommentService.save(imgComment, userDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(ic);
    }

    // 특정 갤러리 게시글의 댓글 전체 조회
    @GetMapping
    public ResponseEntity<List<ImgCommentDTO>> findAll(
            @RequestParam(name = "imgBoardNo") Long imgBoardNo) {
        return ResponseEntity.ok(imgCommentService.findAll(imgBoardNo));
    }

    // 댓글 수정
    @PutMapping("/{imgCommentNo}")
    public ResponseEntity<Void> update(
            @PathVariable("imgCommentNo") Long imgCommentNo,
            @RequestBody ImgCommentDTO imgComment,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        String loginId = userDetails.getUsername();
        imgCommentService.update(imgCommentNo, imgComment.getImgCommentContent(), loginId);
        return ResponseEntity.ok().build();
    }

    // 댓글 삭제
    @DeleteMapping("/{imgCommentNo}")
    public ResponseEntity<Void> delete(
            @PathVariable("imgCommentNo") Long imgCommentNo,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        String loginId = userDetails.getUsername();
        imgCommentService.delete(imgCommentNo, loginId);
        return ResponseEntity.noContent().build();
    }

    // 댓글 신고
    @PostMapping("/{imgCommentNo}/report")
    public ResponseEntity<Void> report(
            @PathVariable("imgCommentNo") Long imgCommentNo,
            @RequestBody ReportRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        String loginId = userDetails.getUsername();
        imgCommentService.report(imgCommentNo, loginId, request.getReason());
        return ResponseEntity.ok().build();
    }

    @Data
    public static class ReportRequest {
        private String reason;
    }
}
