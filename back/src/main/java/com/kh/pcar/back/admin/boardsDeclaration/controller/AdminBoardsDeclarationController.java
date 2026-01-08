package com.kh.pcar.back.admin.boardsDeclaration.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.pcar.back.admin.boardsDeclaration.model.dto.AdminBoardsDeclarationDTO;
import com.kh.pcar.back.admin.boardsDeclaration.model.service.AdminBoardsDeclarationService;
import com.kh.pcar.back.common.ResponseData;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/community")
public class AdminBoardsDeclarationController {

    private final AdminBoardsDeclarationService adminBoardsDeclarationService;

    // 1. 게시글 신고 목록 조회
    @GetMapping("/declaration")
    public ResponseEntity<ResponseData<List<AdminBoardsDeclarationDTO>>> findAllDeclaration() {
        List<AdminBoardsDeclarationDTO> dto = adminBoardsDeclarationService.findAllDeclaration();
        // 데이터와 성공 메시지를 함께 전달 (200 OK)
        return ResponseData.ok(dto, "신고 목록을 성공적으로 불러왔습니다.");
    }

    // 2. 게시글 신고 삭제 (204 No Content)
    @DeleteMapping("/declaration/delete/{reportNo}")
    public ResponseEntity<ResponseData<Void>> deleteDeclaration(@PathVariable(name = "reportNo") Long reportNo) {
        adminBoardsDeclarationService.deleteDeclaration(reportNo);
        // 삭제 성공 시 본문 없이 상태 코드만 전달
        return ResponseData.noContent();
    }

    // 3. 게시글 신고 반려 (200 OK + 메시지)
    @PutMapping("/declaration/reject/{reportNo}")
    public ResponseEntity<ResponseData<Void>> rejectDeclaration(@PathVariable(name="reportNo") Long reportNo) {
        adminBoardsDeclarationService.rejectDeclaration(reportNo);
        // '거절 성공'이라는 메시지를 보내야 하므로 ok 사용
        return ResponseData.ok(null, "거절 성공");
    }
    
    // 4. 댓글 신고 조회
    @GetMapping("/comment/declaration")
    public ResponseEntity<ResponseData<List<AdminBoardsDeclarationDTO>>> findAllCommentDeclaration() {
        List<AdminBoardsDeclarationDTO> comment = adminBoardsDeclarationService.findAllCommentDeclaration();
        return ResponseData.ok(comment, "조회 성공");
    }
    
    // 5. 댓글 신고 삭제 (204 No Content)
    @DeleteMapping("/comment/declaration/delete/{reportNo}")
    public ResponseEntity<ResponseData<Void>> deleteCommentDeclaration(@PathVariable(name="reportNo") Long reportNo) {
        adminBoardsDeclarationService.deleteDeclaration(reportNo);
        return ResponseData.noContent();
    }
    
    // 6. 댓글 신고 반려 (204 No Content 제안)
    @PutMapping("/comment/declaration/reject/{reportNo}")
    public ResponseEntity<ResponseData<Void>> rejectCommentDeclaration(@PathVariable(name="reportNo") Long reportNo) {
        adminBoardsDeclarationService.rejectDeclaration(reportNo);
        // 메시지가 굳이 필요 없다면 noContent()가 더 표준적입니다.
        return ResponseData.noContent();
    }

	
}