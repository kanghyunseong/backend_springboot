package com.kh.pcar.back.boards.board.model.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.kh.pcar.back.auth.model.vo.CustomUserDetails;
import com.kh.pcar.back.boards.board.model.dto.BoardDTO;

public interface BoardService {
	
	void save(BoardDTO board, MultipartFile file, String username);
	
	List <BoardDTO> findAll(int pageNo);
	
	BoardDTO findByBoardNo(Long bardNo);
	
	BoardDTO update(BoardDTO board, MultipartFile file, Long boardNo, CustomUserDetails userDetails);
	
	void deleteByBoardNo(Long boardNo, CustomUserDetails userDetails);
}
