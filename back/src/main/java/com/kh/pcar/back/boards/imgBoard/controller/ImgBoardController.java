package com.kh.pcar.back.boards.imgBoard.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kh.pcar.back.auth.model.vo.CustomUserDetails;
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
	
	// 게시글 작성 + 첨부파일이 있는
	@PostMapping
	public ResponseEntity<?> save(@Valid ImgBoardDTO imgBoard, 
			@RequestParam(name="file", required=false) MultipartFile file,
			@AuthenticationPrincipal CustomUserDetails userDetails){
		if (userDetails == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	    }
		
		log.info("게시글 정보 : {}, 파일정보 : {}", imgBoard, file.getOriginalFilename());
		imgBoardService.imgSave(imgBoard, file, userDetails.getUsername());
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	// 전체조회
	// GET boards
	@GetMapping
	public ResponseEntity<List<ImgBoardDTO>> imgFindAll(@RequestParam(name="page", defaultValue="0")int pageNo){
		List<ImgBoardDTO>imgBoards = imgBoardService.imgFindAll(pageNo);
		return ResponseEntity.ok(imgBoards);
	}
	
	// 단일조회
	// GET /boards/ primaryKey
	@GetMapping("/{imgBoardNo}")
	public ResponseEntity<ImgBoardDTO> findByImgBoardNo(@PathVariable(name="imgBoardNo") 
												  @Min(value=1, message="넘작아용") Long imgBoardNo){
		ImgBoardDTO imgBoard = imgBoardService.findByImgBoardNo(imgBoardNo);
		return ResponseEntity.ok(imgBoard);
	}
	
	@PutMapping("/{imgBoardNo}")
	public ResponseEntity<ImgBoardDTO> imgUpdate(@PathVariable(name="imgBoardNo") Long imgBoardNo,
										   ImgBoardDTO imgBoard, @RequestParam(name="file", required=false)
										   MultipartFile file, 
										   @AuthenticationPrincipal CustomUserDetails userDetails){
		if (userDetails == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	    }
		
		imgBoard.setImgBoardNo(imgBoardNo);
		imgBoardService.imgUpdate(imgBoard, file, imgBoardNo, userDetails);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@DeleteMapping("/{imgBoardNo}")
	public ResponseEntity<?> deleteByImgBoardNo(@PathVariable(name="imgBoardNo") Long imgBoardNo,
											 @AuthenticationPrincipal CustomUserDetails userDetails){
		if (userDetails == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	    }
		
		imgBoardService.deleteByImgBoardNo(imgBoardNo, userDetails);
		return ResponseEntity.ok().build();
	}
}
