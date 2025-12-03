package com.kh.pcar.back.boards.imgBoard.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kh.pcar.back.auth.model.vo.CustomUserDetails;
import com.kh.pcar.back.boards.PageResponseDTO;
import com.kh.pcar.back.boards.Report.dto.ReportDTO;
import com.kh.pcar.back.boards.Report.service.ReportService;
import com.kh.pcar.back.boards.imgBoard.model.dto.ImgBoardDTO;
import com.kh.pcar.back.boards.imgBoard.model.service.ImgBoardService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Validated
@RequestMapping("boards/imgBoards")
@RequiredArgsConstructor
public class ImgBoardController {
	private final ImgBoardService imgBoardService;
	private final ReportService reportService;
	
	// 게시글 작성 + 첨부파일
	@PostMapping
	public ResponseEntity<?> save(
	        @Valid ImgBoardDTO imgBoard,
	        @RequestParam(name = "files", required = false) MultipartFile[] files,
	        @AuthenticationPrincipal CustomUserDetails userDetails
	) {
	    if (userDetails == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	    }

	    log.info("게시글 정보 : {}, 업로드 파일 개수 : {}", imgBoard, 
	             (files != null ? files.length : 0));

	    imgBoardService.imgSave(imgBoard, files, userDetails.getUsername());

	    return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	// 전체조회
	// GET boards
	@GetMapping
	public ResponseEntity<PageResponseDTO<ImgBoardDTO>> imgFindAll(@RequestParam(name="page", defaultValue="0")int pageNo){
		return ResponseEntity.ok(imgBoardService.imgFindAll(pageNo));
	}
	
	@GetMapping("/search")
	public ResponseEntity<PageResponseDTO<ImgBoardDTO>> searchImgBoards(
	        @RequestParam(name = "type") String type,
	        @RequestParam(name = "keyword") String keyword,
	        @RequestParam(name = "page", defaultValue = "0") int pageNo) {
	    log.info("검색 요청 - type: {}, keyword: {}, page: {}", type, keyword, pageNo);

	    PageResponseDTO<ImgBoardDTO> result = imgBoardService.searchImgBoards(type, keyword, pageNo);

	    return ResponseEntity.ok(result);
	}
	
	// 단일조회
	// GET /boards/ primaryKey
	@GetMapping("/{imgBoardNo}")
	public ResponseEntity<ImgBoardDTO> findByImgBoardNo(@PathVariable(name="imgBoardNo") 
												  @Min(value=1, message="넘작아용") Long imgBoardNo){
		imgBoardService.increaseImgView(imgBoardNo);
		ImgBoardDTO imgBoard = imgBoardService.findByImgBoardNo(imgBoardNo);
		return ResponseEntity.ok(imgBoard);
	}
	
	@PutMapping("/{imgBoardNo}")
	public ResponseEntity<ImgBoardDTO> imgUpdate(@PathVariable(name="imgBoardNo") Long imgBoardNo,
										   ImgBoardDTO imgBoard, @RequestParam(name="files", required=false)
										   MultipartFile[] files, 
										   @AuthenticationPrincipal CustomUserDetails userDetails){
		if (userDetails == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	    }
		
		imgBoard.setImgBoardNo(imgBoardNo);
		ImgBoardDTO updated = imgBoardService.imgUpdate(imgBoard, files, imgBoardNo, userDetails);
		
		return ResponseEntity.ok(updated);
	}
	
	@DeleteMapping("/{imgBoardNo}")
	public ResponseEntity<Void> deleteByImgBoardNo(@PathVariable(name="imgBoardNo") Long imgBoardNo,
									@AuthenticationPrincipal CustomUserDetails userDetails) {
		
	    imgBoardService.deleteByImgBoardNo(imgBoardNo, userDetails);
	    return ResponseEntity.noContent().build(); 
	}
	
	@PostMapping("/{imgBoardNo}/report")
	public ResponseEntity<?> reportBoard(@PathVariable(name="imgBoardNo") Long imgBoardNo,
	                                     @RequestBody ReportDTO request,
	                                     @AuthenticationPrincipal CustomUserDetails userDetails) {
	    if (userDetails == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	    }

	    // 1. 게시글 정보 조회 (작성자 USER_NO 필요)
	    ImgBoardDTO imgBoard = imgBoardService.findByImgBoardNo(imgBoardNo);
	    log.info("정보조회 : {} " ,imgBoard );
	    Long reportedUser = imgBoard.getImgWriterNo(); // 신고 당하는 사람 (게시글 작성자)
	    
	    Long reporter = userDetails.getUserNo(); // 신고하는 사람

	    ReportDTO dto = ReportDTO.builder()
	            .targetType("IMGBOARD")
	            .targetNo(imgBoardNo)
	            .reportedUser(reportedUser)
	            .reason(request.getReason()) // 프론트에서 넘어온 신고 사유
	            .build();
	    
	    try {
	        reportService.report(reporter, dto);
	    } catch (IllegalStateException e) {
	        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
	    }
	    
	    return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
