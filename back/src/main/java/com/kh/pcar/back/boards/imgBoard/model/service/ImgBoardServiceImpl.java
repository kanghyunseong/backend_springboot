package com.kh.pcar.back.boards.imgBoard.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kh.pcar.back.auth.model.vo.CustomUserDetails;
import com.kh.pcar.back.boards.imgBoard.model.dao.ImgBoardMapper;
import com.kh.pcar.back.boards.imgBoard.model.dto.ImgBoardDTO;
import com.kh.pcar.back.boards.imgBoard.model.dto.ImgPageResponseDTO;
import com.kh.pcar.back.boards.imgBoard.model.vo.ImgBoardVO;
import com.kh.pcar.back.exception.CustomAuthenticationException;
import com.kh.pcar.back.file.service.FileService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImgBoardServiceImpl implements ImgBoardService {

	private final ImgBoardMapper imgBoardMapper;
	private final FileService fileService;
	private final int pageSize = 10;
	
	@Override
	public void imgSave(ImgBoardDTO imgBoard, MultipartFile file, String userId) {
		
		// 유효성 검증 valid로 퉁
		// 권한검증 -> ROLE로함
		ImgBoardVO ib = null;
		// 첨부파일 관련 값
		if(file != null && !file.isEmpty()) {
			
			String filePath = fileService.store(file);
			
			ib = ImgBoardVO.builder()
							   .imgBoardTitle(imgBoard.getImgBoardTitle())
							   .imgBoardContent(imgBoard.getImgBoardContent())
							   .imgBoardWriter(userId)
							   .fileUrl(filePath)
							   .build();
			// title, content, writer, file INSERT
			
		} else {
			ib = ImgBoardVO.builder().imgBoardTitle(imgBoard.getImgBoardTitle())
					   .imgBoardContent(imgBoard.getImgBoardContent())
					   .imgBoardWriter(userId)
					   .build();
		}
		imgBoardMapper.imgSave(ib);
		
		
	}

	@Override
	public ImgPageResponseDTO<ImgBoardDTO> imgFindAll(int pageNo) {
		 int size = 10;
	    int offset = pageNo * size;


	    // RowBounds 정상 계산
	    RowBounds rb = new RowBounds(offset, size);

	    // 현재 페이지 목록
	    List<ImgBoardDTO> list = imgBoardMapper.imgFindAll(rb);

	    // 전체 개수
	    long total = imgBoardMapper.countImgBoards();

	    // 총 페이지 수
	    int totalPages = (int) Math.ceil(total / (double) size);

	    return new ImgPageResponseDTO<>(
	            list,
	            totalPages,
	            total,
	            pageNo,
	            size
	    );
	}
	
	@Override
    public ImgPageResponseDTO<ImgBoardDTO> searchImgBoards(String type, String keyword, int pageNo) {

        log.info("검색 service - type: {}, keyword: {}, page: {}", type, keyword, pageNo);

        int offset = pageNo * pageSize;

        // MyBatis에 넘길 파라미터
        Map<String, Object> params = new HashMap<>();
        params.put("type", type);                         // title / writer / content
        params.put("keyword", "%" + keyword + "%");       // LIKE 검색용
        params.put("offset", offset);
        params.put("pageSize", pageSize);

        // 목록 조회
        List<ImgBoardDTO> list = imgBoardMapper.searchImgBoards(params);

        // 전체 개수 조회
        int totalCount = imgBoardMapper.countSearchImgBoards(params);

        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        return new ImgPageResponseDTO<>(list, pageNo, pageSize, totalPages, totalCount);
    }
	

	@Override
	public ImgBoardDTO findByImgBoardNo(Long imgBoardNo) {
		return getImgBoardOrThrow(imgBoardNo);
	}
	
	@Override
	public void increaseImgView(Long imgBoardNo) {
		imgBoardMapper.increaseImgView(imgBoardNo);
	}
	
	private ImgBoardDTO getImgBoardOrThrow(Long imgBoardNo) {
		return getImgBoardOrThrow(imgBoardNo);
	}
	
	private void validateImgBoard(Long imgBoardNo, CustomUserDetails userDetails) {
		ImgBoardDTO imgBoard = getImgBoardOrThrow(imgBoardNo);
		if(!imgBoard.getImgBoardWriter().equals(userDetails.getUsername())) {
			throw new CustomAuthenticationException("게시글이 존재하지 않습니다.");
		}
	}	
	
	@Override
	public ImgBoardDTO imgUpdate(ImgBoardDTO imgBoard, MultipartFile file
						  ,Long imgBoardNo, CustomUserDetails userDetails) {
		
		// 1. 원본 게시글 조회
		ImgBoardDTO imgOrigin = imgBoardMapper.findByImgBoardNo(imgBoardNo);
	    if (imgOrigin == null) {
	        throw new RuntimeException("게시글이 존재하지 않습니다.");
	    }

	    // 2. 작성자 체크
	    if (!imgOrigin.getImgBoardWriter().equals(userDetails.getUsername())) {
	        throw new RuntimeException("작성자만 수정 가능합니다.");
	    }

	    // 3. 수정 적용
	    imgBoard.setImgBoardNo(imgBoardNo);
	    imgBoard.setImgBoardWriter(imgOrigin.getImgBoardWriter());

	    imgBoardMapper.imgUpdate(imgBoard);

	    // 4. 최신 데이터 다시 조회해서 반환
	    return imgBoardMapper.findByImgBoardNo(imgBoardNo);
	}

	@Override
	public void deleteByImgBoardNo(Long imgBoardNo, CustomUserDetails userDetails) {
		
		validateImgBoard(imgBoardNo, userDetails);
		imgBoardMapper.deleteByImgBoardNo(imgBoardNo);

	}
}
