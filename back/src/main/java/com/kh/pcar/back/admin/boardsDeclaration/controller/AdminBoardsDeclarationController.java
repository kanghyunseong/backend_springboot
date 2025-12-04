package com.kh.pcar.back.admin.boardsDeclaration.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.pcar.back.admin.boardsDeclaration.model.dto.AdminBoardsDeclarationDTO;
import com.kh.pcar.back.admin.boardsDeclaration.model.service.AdminBoardsDeclarationService;
import com.kh.pcar.back.exception.BoardsNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/api/community")
public class AdminBoardsDeclarationController {

	private final AdminBoardsDeclarationService adminBoardsDeclarationService;

	@GetMapping("/declaration")
	public ResponseEntity<List<AdminBoardsDeclarationDTO>> findAllDeclaration() {
		return ResponseEntity.ok(adminBoardsDeclarationService.findAllDeclaration());
	}

	// 신고된 게시글 삭제
	@DeleteMapping("/declaration/delete/{reportNo}")
	public ResponseEntity<String> deleteDeclaration(@PathVariable(name = "reportNo") Long reportNo) {
		
			adminBoardsDeclarationService.deleteDeclaration(reportNo);
			return ResponseEntity.ok("게시글 삭제 성공");
	}

	// 신고된 게시글 반려
	@PutMapping("/declaration/reject/{reportNo}")
	public ResponseEntity<String> rejectDeclaration(@PathVariable(name="reportNo")Long reportNo) {
		 adminBoardsDeclarationService.rejectDeclaration(reportNo);
		return ResponseEntity.ok("게시글 반려 성공 ");
	}
	
	// 댓글 조회
	@GetMapping("/comment/declaration")
	public ResponseEntity<List<AdminBoardsDeclarationDTO>> findAllCommentDeclaration() {
		return ResponseEntity.ok(adminBoardsDeclarationService.findAllCommentDeclaration());
	}
	
	// 댓글 삭제
	@DeleteMapping("/comment/declaration/delete/{reportNo}")
	public ResponseEntity<String> deleteCommentDeclaration(@PathVariable(name="reportNo")Long reportNo) {
			adminBoardsDeclarationService.deleteDeclaration(reportNo);
			return ResponseEntity.ok("댓글 삭제 성공");
	}
	
	@PutMapping("/comment/declaration/reject/{reportNo}")
	public ResponseEntity<String> rejectCommentDeclaration(@PathVariable(name="reportNo")Long reportNo) {
		adminBoardsDeclarationService.rejectDeclaration(reportNo);
		return ResponseEntity.ok("댓글 반려 성공");
	}
}