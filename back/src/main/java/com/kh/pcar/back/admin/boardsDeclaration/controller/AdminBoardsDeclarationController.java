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
		try {
			adminBoardsDeclarationService.deleteDelcaration(reportNo);
			return ResponseEntity.ok("게시글 삭제 성공");
		} catch (BoardsNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시글 삭제 처리에 문제가 생겼습니다.");
		}
	}

	// 신고된 게시글 반려
	@PutMapping("/declaration/reject/{reportNo}")
	public ResponseEntity<String> rejectDeclaration(@PathVariable(name="reportNo")Long reportNo) {
	 try {
		 adminBoardsDeclarationService.rejectDeclaration(reportNo);
		return ResponseEntity.ok("게시글 반려 성공 ");
	 } catch(Exception e) {
		 e.printStackTrace();
		 return ResponseEntity.internalServerError().body("반려 실패 ");
	 }
	}
}
