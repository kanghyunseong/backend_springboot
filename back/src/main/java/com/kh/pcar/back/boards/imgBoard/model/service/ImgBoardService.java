package com.kh.pcar.back.boards.imgBoard.model.service;

import org.springframework.web.multipart.MultipartFile;

import com.kh.pcar.back.auth.model.vo.CustomUserDetails;
import com.kh.pcar.back.boards.board.model.dto.BoardDTO;
import com.kh.pcar.back.boards.imgBoard.model.dto.ImgBoardDTO;
import com.kh.pcar.back.boards.imgBoard.model.dto.ImgPageResponseDTO;

public interface ImgBoardService {
	
	void imgSave(ImgBoardDTO imgBoard, MultipartFile[] files, String username);
	
	ImgPageResponseDTO<ImgBoardDTO> imgFindAll(int PageNo);
	
	ImgPageResponseDTO<ImgBoardDTO> searchImgBoards(String type, String keyword, int pageNo);
	
	ImgBoardDTO findByImgBoardNo(Long imgBoard);
	
	void increaseImgView(Long id);
	
	ImgBoardDTO imgUpdate(ImgBoardDTO imgBoard, MultipartFile[] files, Long imgBoardNo, CustomUserDetails userDetails);
	
	void deleteByImgBoardNo(Long imgBoardNo, CustomUserDetails userDetails);
}
