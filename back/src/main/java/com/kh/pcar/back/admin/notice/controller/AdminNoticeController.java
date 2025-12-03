package com.kh.pcar.back.admin.notice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.pcar.back.admin.notice.model.dto.AdminNoticeDTO;
import com.kh.pcar.back.admin.notice.model.service.AdminNoticeService;
import com.kh.pcar.back.exception.NoticeNotFoundException;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/api/notice")
public class AdminNoticeController {

	private final AdminNoticeService adminNoticeService;

	@GetMapping("/list")
	public ResponseEntity<List<AdminNoticeDTO>> findAllNotice() {
		List<AdminNoticeDTO> noticeList = adminNoticeService.findAllNotice();
		return ResponseEntity.ok(noticeList);
	}

	@GetMapping("/{noticeNo}")
	public ResponseEntity<Object> getNoticeNo(@PathVariable(name = "noticeNo") Long noticeNo) {
		return ResponseEntity.ok(adminNoticeService.findNoticeByNo(noticeNo));
	}

	@DeleteMapping("/delete/{noticeNo}")
	public ResponseEntity<String> deleteNotice(@PathVariable(name = "noticeNo") Long noticeNo) {
		try {
			adminNoticeService.deleteNotice(noticeNo);
			return ResponseEntity.ok("공지사항 삭제에 성공하셨습니다.");
		} catch (NoticeNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("공지사항 삭제 처리에 문제가 생겼습니다.");
		}
	}

	@PostMapping("insert")
	public ResponseEntity<String> registerNotice(@RequestBody AdminNoticeDTO adminNoticeDTO) {

		try {
			adminNoticeService.registerNotice(adminNoticeDTO);
			return ResponseEntity.ok("공지사항 등록 성공");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("등록 실패: " + e.getMessage());
		}
	}
	
	@PutMapping("/modify")
	public ResponseEntity<String> modifyNotice(@RequestBody AdminNoticeDTO adminNoticeDTO) {
		try {
			adminNoticeService.modifyNotice(adminNoticeDTO);
			return ResponseEntity.ok("공지사항 수정 성공");
		} catch(NoticeNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("수정 실패: " + e.getMessage());
		}
	}
}
