package com.kh.pcar.back.boards.board.model.service;

import java.security.InvalidParameterException;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kh.pcar.back.auth.model.vo.CustomUserDetails;
import com.kh.pcar.back.boards.board.model.dao.BoardMapper;
import com.kh.pcar.back.boards.board.model.dto.BoardDTO;
import com.kh.pcar.back.boards.board.model.vo.BoardVO;
import com.kh.pcar.back.exception.CustomAuthenticationException;
import com.kh.pcar.back.file.service.FileService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
	
	private final BoardMapper boardMapper;
	
	@Override
	public void save(BoardDTO board, String userId) {
		board.setBoardWriter(userId);
		// 유효성 검증 valid로 퉁
		// 권한검증 -> ROLE로함
		BoardVO b = null;
		b = BoardVO.builder()
						   .boardTitle(board.getBoardTitle())
						   .boardContent(board.getBoardContent())
						   .boardWriter(userId)
						   .build();
		boardMapper.save(b);
	}

	@Override
	public List<BoardDTO> findAll(int pageNo) {
		if(pageNo < 0) {
			throw new InvalidParameterException("유효하지 않은 접근입니다.");
		}
		RowBounds rb = new RowBounds((pageNo - 1) * 10, 10);
		return boardMapper.findAll(rb);
	}

	@Override
	public BoardDTO findByBoardNo(Long boardNo) {
		return getBoardOrThrow(boardNo);
	}
	
	private BoardDTO getBoardOrThrow(Long boardNo) {
		BoardDTO board = boardMapper.findByBoardNo(boardNo);
		if(board == null) {
			throw new InvalidParameterException("유효하지 않은 접근입니다.");
		}
		return board;
	}
	
	private void validateBoard(Long boardNo, CustomUserDetails userDetails) {
		BoardDTO board = getBoardOrThrow(boardNo);
		if(!board.getBoardWriter().equals(userDetails.getUserId())) {
			throw new CustomAuthenticationException("게시글이 존재하지 않습니다.");
		}
	}
	
	@Override
	public BoardDTO update(BoardDTO board, Long boardNo, 
									CustomUserDetails userDetails) {
		validateBoard(boardNo, userDetails);
		board.setBoardNo(boardNo);
		boardMapper.update(board);
		return board;
	}
	
	@Override
	public void deleteByBoardNo(Long boardNo, CustomUserDetails userDetails) {
		validateBoard(boardNo, userDetails);
		boardMapper.deleteByBoardNo(boardNo);
	}
}
