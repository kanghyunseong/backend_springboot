package com.kh.pcar.back.boards.board.model.service;

import com.kh.pcar.back.auth.model.vo.CustomUserDetails;
import com.kh.pcar.back.boards.PageResponseDTO;
import com.kh.pcar.back.boards.board.model.dto.BoardDTO;

public interface BoardService {
	
	void save(BoardDTO board, String userId);
	
	PageResponseDTO<BoardDTO> findAll(int pageNo);
	
	PageResponseDTO<BoardDTO> searchBoards(String type, String keyword, int pageNo);
	
	BoardDTO findByBoardNo(Long bardNo);
	
	void increaseView(Long id);
	
	BoardDTO update(BoardDTO board, Long boardNo, CustomUserDetails userDetails);
	
	void deleteByBoardNo(Long boardNo, CustomUserDetails userDetails);
}
