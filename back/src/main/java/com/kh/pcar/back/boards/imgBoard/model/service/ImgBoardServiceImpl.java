package com.kh.pcar.back.boards.imgBoard.model.service;

import java.security.InvalidParameterException;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kh.pcar.back.auth.model.vo.CustomUserDetails;
import com.kh.pcar.back.boards.imgBoard.model.dao.ImgBoardMapper;
import com.kh.pcar.back.boards.imgBoard.model.dto.ImgBoardDTO;
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
	public List<ImgBoardDTO> imgFindAll(int pageNo) {
		if(pageNo < 0) {
			throw new InvalidParameterException("유효하지 않은 접근입니다.");
		}
		RowBounds rb = new RowBounds((pageNo - 1 * 10), 10);
		return imgBoardMapper.imgFindAll(rb);
	}

	@Override
	public ImgBoardDTO findByImgBoardNo(Long imgBoardNo) {
		return getImgBoardOrThrow(imgBoardNo);
	}
	
	private ImgBoardDTO getImgBoardOrThrow(Long imgBoardNo) {
		ImgBoardDTO imgBoard = imgBoardMapper.findByImgBoardNo(imgBoardNo);
		if(imgBoard == null) {
			throw new InvalidParameterException("유효하지 않은 접근입니다.");
		}
		return imgBoard;
	}
	
	@Override
	public ImgBoardDTO imgUpdate(ImgBoardDTO imgBoard, MultipartFile file
						  ,Long imgBoardNo, CustomUserDetails userDetails) {
		
		validateImgBoard(imgBoardNo, userDetails);
		imgBoard.setImgBoardNo(imgBoardNo);
		if(file != null && !file.isEmpty()) {
			String filePath = fileService.store(file);
			imgBoard.setFileUrl(filePath);
		}
		imgBoardMapper.imgUpdate(imgBoard);
		return imgBoard;
	}
	
	private void validateImgBoard(Long imgBoardNo, CustomUserDetails userDetails) {
		ImgBoardDTO imgBoard = getImgBoardOrThrow(imgBoardNo);
		if(!imgBoard.getImgBoardWriter().equals(userDetails.getUserId())) {
			throw new CustomAuthenticationException("게시글이 존재하지 않습니다.");
		}
	}

	@Override
	public void deleteByImgBoardNo(Long imgBoardNo, CustomUserDetails userDetails) {
		
		validateImgBoard(imgBoardNo, userDetails);
		imgBoardMapper.deleteByImgBoardNo(imgBoardNo);

	}
}
