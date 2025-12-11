package com.kh.pcar.back.boards.board.model.service;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import com.kh.pcar.back.auth.model.vo.CustomUserDetails;
import com.kh.pcar.back.boards.PageResponseDTO;
import com.kh.pcar.back.boards.board.model.dao.BoardMapper;
import com.kh.pcar.back.boards.board.model.dto.BoardDTO;
import com.kh.pcar.back.boards.board.model.vo.BoardVO;
import com.kh.pcar.back.exception.CustomAuthorizationException;

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
		// 유효성 검증 valid로 퉁
		// 권한검증 -> ROLE로함
		BoardVO b =BoardVO.builder()
						   .boardTitle(board.getBoardTitle())
						   .boardContent(board.getBoardContent())
						   .boardWriter(userId)
						   .build();
		boardMapper.save(b);
	}

	@Override
	public PageResponseDTO<BoardDTO> findAll(int pageNo) {

	    int size = pageSize;
	    int offset = pageNo * size;


	    // RowBounds 정상 계산
	    RowBounds rb = new RowBounds(offset, size);

	    // 현재 페이지 목록
	    List<BoardDTO> list = boardMapper.findAll(rb);

	    // 전체 개수
	    long total = boardMapper.countBoards();

	    return new PageResponseDTO<>(list, total, pageNo, size);
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

        return new PageResponseDTO<>(list, totalCount, pageNo, pageSize);
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
			throw new CustomAuthorizationException("작성자만 수정/삭제할 수 있습니다.");
		}
	}
	
	@Override
	public BoardDTO update(BoardDTO board, Long boardNo, CustomUserDetails userDetails) {
		
	    // 1. 존재 여부 체크 (없으면 InvalidParameterException -> 400)
	    BoardDTO origin = getBoardOrThrow(boardNo);

	    // 2. 권한 체크 (작성자만 수정 가능 – 403)
	    if (!origin.getBoardWriter().equals(userDetails.getUsername())) {
	        throw new CustomAuthorizationException("작성자만 수정 가능합니다.");
	    }

	    // 3. 수정 적용
	    origin.setBoardTitle(board.getBoardTitle());
	    origin.setBoardContent(board.getBoardContent());

	    boardMapper.update(origin);

	    // 4. 최신 데이터 다시 조회해서 반환
	    return boardMapper.findByBoardNo(boardNo);
	}

	
	@Override
	public void deleteByBoardNo(Long boardNo, CustomUserDetails userDetails) {
		validateBoard(boardNo, userDetails);
		boardMapper.deleteByBoardNo(boardNo);
	}
}
