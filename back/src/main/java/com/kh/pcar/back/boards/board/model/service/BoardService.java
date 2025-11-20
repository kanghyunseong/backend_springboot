package com.kh.pcar.back.boards.board.model.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.kh.pcar.back.auth.model.vo.CustomUserDetails;
import com.kh.pcar.back.boards.board.model.dto.BoardDTO;
import com.kh.pcar.back.boards.board.model.dto.PageResponse;

public interface BoardService {
	
	void save(BoardDTO board, String userId);
	
	PageResponse<BoardDTO> findAll(int pageNo);
	
	BoardDTO findByBoardNo(Long bardNo);
	
	BoardDTO update(BoardDTO board, Long boardNo, CustomUserDetails userDetails);
	
	void deleteByBoardNo(Long boardNo, CustomUserDetails userDetails);
}
