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
import com.kh.pcar.back.boards.comment.model.dto.CommentDTO;
import com.kh.pcar.back.boards.comment.model.service.CommentService;
import com.kh.pcar.back.boards.comment.model.vo.CommentVO;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
	
	private final CommentService commentService;
	
	@PostMapping
	public ResponseEntity<?> save(@RequestBody CommentDTO comment,
								  @AuthenticationPrincipal CustomUserDetails userDetails){
		CommentDTO c = commentService.save(comment,  userDetails);
		return ResponseEntity.status(HttpStatus.CREATED).body(c);
	}
	
	@GetMapping
	public ResponseEntity<List<CommentDTO>> findAll(@RequestParam(name="boardNo") Long boardNo){
		return ResponseEntity.ok(commentService.findAll(boardNo));
	}
	
	@PutMapping("/{commentNo}")
    public ResponseEntity<Void> update(
            @PathVariable(name="commentNo") Long commentNo,
            @RequestBody CommentDTO dto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        String loginId = userDetails.getUsername();
        commentService.update(commentNo, dto.getCommentContent(), loginId);
        return ResponseEntity.ok().build();
    }

    // DELETE /comments/{commentNo}
    @DeleteMapping("/{commentNo}")
    public ResponseEntity<Void> delete(
            @PathVariable(name="commentNo") Long commentNo,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        String loginId = userDetails.getUsername();
        commentService.delete(commentNo, loginId);
        return ResponseEntity.noContent().build();
    }

    // POST /comments/{commentNo}/report
    @PostMapping("/{commentNo}/report")
    public ResponseEntity<Void> report(
            @PathVariable(name="commentNo") Long commentNo,
            @RequestBody ReportRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails    ) {
        String loginId = userDetails.getUsername();
        commentService.report(commentNo, loginId, request.getReason());
        return ResponseEntity.ok().build();
    }

    @Data
    public static class ReportRequest {
        private String reason;
    }
	

}
