package com.kh.pcar.back.boards.board.model.service;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import com.kh.pcar.back.auth.model.vo.CustomUserDetails;
import com.kh.pcar.back.boards.board.model.dao.BoardMapper;
import com.kh.pcar.back.boards.board.model.dto.BoardDTO;
import com.kh.pcar.back.boards.board.model.dto.PageResponseDTO;
import com.kh.pcar.back.boards.board.model.vo.BoardVO;
import com.kh.pcar.back.exception.CustomAuthenticationException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
	
	private final BoardMapper boardMapper;
	
	private final int pageSize = 10;
	
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
	public PageResponseDTO<BoardDTO> findAll(int pageNo) {

	    if (pageNo < 0) {
	        throw new InvalidParameterException("유효하지 않은 접근입니다.");
	    }

	    RowBounds rb = new RowBounds(pageNo * pageSize, pageSize);
	    List<BoardDTO> list = boardMapper.findAll(rb);

	    int totalCount = boardMapper.countBoards();
	    int totalPages = (int) Math.ceil((double) totalCount / pageSize);

	    return new PageResponseDTO<>(
	            list,
	            pageNo,
	            pageSize,
	            totalPages,
	            totalCount
	    );
	}
	
	
	@Override
    public PageResponseDTO<BoardDTO> searchBoards(String type, String keyword, int pageNo) {

        log.info("검색 service - type: {}, keyword: {}, page: {}", type, keyword, pageNo);

        int offset = pageNo * pageSize;

        // MyBatis에 넘길 파라미터
        Map<String, Object> params = new HashMap<>();
        params.put("type", type);                         // title / writer / content
        params.put("keyword", "%" + keyword + "%");       // LIKE 검색용
        params.put("offset", offset);
        params.put("pageSize", pageSize);

        // 목록 조회
        List<BoardDTO> list = boardMapper.searchBoards(params);

        // 전체 개수 조회
        int totalCount = boardMapper.countSearchBoards(params);

        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        return new PageResponseDTO<>(list, pageNo, pageSize, totalPages, totalCount);
    }

	
	@Override
	public BoardDTO findByBoardNo(Long boardNo) {
		return getBoardOrThrow(boardNo);
	}
	
	@Override
	public void increaseView(Long boardNo) {
	    boardMapper.increaseView(boardNo);
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
		if(!board.getBoardWriter().equals(userDetails.getUsername())) {
			throw new CustomAuthenticationException("게시글이 존재하지 않습니다.");
		}
	}
	
	@Override
	public BoardDTO update(BoardDTO board, Long boardNo, 
									CustomUserDetails userDetails) {
		// 1. 원본 게시글 조회
	    BoardDTO origin = boardMapper.findByBoardNo(boardNo);
	    if (origin == null) {
	        throw new RuntimeException("게시글이 존재하지 않습니다.");
	    }

	    // 2. 작성자 체크
	    if (!origin.getBoardWriter().equals(userDetails.getUsername())) {
	        throw new RuntimeException("작성자만 수정 가능합니다.");
	    }

	    // 3. 수정 적용
	    board.setBoardNo(boardNo);
	    board.setBoardWriter(origin.getBoardWriter());

	    boardMapper.update(board);

	    // 4. 최신 데이터 다시 조회해서 반환
	    return boardMapper.findByBoardNo(boardNo);
	}
	
	@Override
	public void deleteByBoardNo(Long boardNo, CustomUserDetails userDetails) {
		validateBoard(boardNo, userDetails);
		boardMapper.deleteByBoardNo(boardNo);
	}
}
