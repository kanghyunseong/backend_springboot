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
import com.kh.pcar.back.common.ResponseData;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/notice")
public class AdminNoticeController {

	private final AdminNoticeService adminNoticeService;

	@GetMapping("/list")
	public ResponseEntity<ResponseData<List<AdminNoticeDTO>>> findAllNotice() {
		List<AdminNoticeDTO> noticeList = adminNoticeService.findAllNotice();
		
		return ResponseData.ok(noticeList, "목록 조회 성공");
	}

	@GetMapping("/{noticeNo}")
	public ResponseEntity<ResponseData<AdminNoticeDTO>> getNotice(@PathVariable(name = "noticeNo") Long noticeNo) {
	    // DB에서 상세 정보를 조회
	    AdminNoticeDTO notice = adminNoticeService.findNoticeByNo(noticeNo);
	    
	    // 조회된 notice 객체를 응답 데이터에 담아서 반환!
	    return ResponseData.ok(notice, "공지사항 상세조회 성공");
	}
	

	@DeleteMapping("/delete/{noticeNo}")
	public ResponseEntity<ResponseData<Object>> deleteNotice(@PathVariable(name = "noticeNo") Long noticeNo) {
			
		adminNoticeService.deleteNotice(noticeNo);
			
			return ResponseData.noContent();
	}

	@PostMapping("/insert")
	public ResponseEntity<ResponseData<String>> registerNotice(@RequestBody AdminNoticeDTO adminNoticeDTO) {

			adminNoticeService.registerNotice(adminNoticeDTO);
			
			return ResponseData.created("공지사항 등록 성공");
	}
	
	@PutMapping("/modify")
	public ResponseEntity<ResponseData<Object>> modifyNotice(@RequestBody AdminNoticeDTO adminNoticeDTO) {
			
		adminNoticeService.modifyNotice(adminNoticeDTO);
			
		return ResponseData.ok(null, "공지사항 수정 성공");
	}
}
