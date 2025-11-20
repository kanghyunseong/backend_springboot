package com.kh.pcar.back.boards.board.controller;

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

import com.kh.pcar.back.auth.model.vo.CustomUserDetails;
import com.kh.pcar.back.boards.board.model.dto.BoardDTO;
import com.kh.pcar.back.boards.board.model.dto.PageResponseDTO;
import com.kh.pcar.back.boards.board.model.service.BoardService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Validated
@RequestMapping("boards/boards")
@RequiredArgsConstructor
public class BoardController {
	
private final BoardService boardService;
	
	// 게시글 작성
	@PostMapping
	public ResponseEntity<?> save(@Valid BoardDTO board, 
			@AuthenticationPrincipal CustomUserDetails userDetails){
		
		if (userDetails == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	    }
		
		log.info("게시글 정보 : {}", board);
		boardService.save(board, userDetails.getUsername());
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	// 전체조회
	// GET boards
	@GetMapping
	public ResponseEntity<PageResponseDTO<BoardDTO>> findAll(
	        @RequestParam(name = "page", defaultValue = "0") int pageNo) {
		
	    return ResponseEntity.ok(boardService.findAll(pageNo));
	}
	
	@GetMapping("/search")
	public ResponseEntity<PageResponseDTO<BoardDTO>> searchBoards(
	        @RequestParam(name = "type") String type,
	        @RequestParam(name = "keyword") String keyword,
	        @RequestParam(name = "page", defaultValue = "0") int pageNo) {
	    log.info("검색 요청 - type: {}, keyword: {}, page: {}", type, keyword, pageNo);

	    PageResponseDTO<BoardDTO> result = boardService.searchBoards(type, keyword, pageNo);

	    return ResponseEntity.ok(result);
	}
	
	// 단일조회
	// GET /boards/ primaryKey
	@GetMapping("/{boardNo}")
	public ResponseEntity<BoardDTO> findByBoardNo(@PathVariable(name="boardNo") 
												  @Min(value=1, message="넘작아용") Long boardNo){
		boardService.increaseView(boardNo);
		BoardDTO board = boardService.findByBoardNo(boardNo);
		return ResponseEntity.ok(board);
	}
	
	@PutMapping("/{boardNo}")
	public ResponseEntity<BoardDTO> update(@PathVariable(name="boardNo") Long boardNo,
										   @RequestBody BoardDTO board,
										   @AuthenticationPrincipal CustomUserDetails userDetails){
		if (userDetails == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	    }
		
		BoardDTO update = boardService.update(board, boardNo, userDetails);
	    return ResponseEntity.ok(update);
	}
	
	@DeleteMapping("/{boardNo}")
	public ResponseEntity<?> deleteBuBoardNo(@PathVariable(name="boardNo") Long boardNo,
											 @AuthenticationPrincipal CustomUserDetails userDetails){
		if (userDetails == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	    }
		
		boardService.deleteByBoardNo(boardNo, userDetails);
		return ResponseEntity.ok().build();
	}
}

