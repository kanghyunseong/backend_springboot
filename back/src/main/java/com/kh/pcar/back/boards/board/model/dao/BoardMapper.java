package com.kh.pcar.back.boards.board.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.kh.pcar.back.boards.board.model.dto.BoardDTO;
import com.kh.pcar.back.boards.board.model.vo.BoardVO;

@Mapper
public interface BoardMapper {
	
	void save(BoardVO board);
	
	List<BoardDTO> findAll(RowBounds rb);
	
	int countBoards();

    List<BoardDTO> searchBoards(Map<String, Object> params);

    int countSearchBoards(Map<String, Object> params);

	BoardDTO findByBoardNo(Long boardNo);
	
	void increaseView(Long boardNo);
	
	void update(BoardDTO board);
	
	void deleteByBoardNo(Long boardNo);
}
