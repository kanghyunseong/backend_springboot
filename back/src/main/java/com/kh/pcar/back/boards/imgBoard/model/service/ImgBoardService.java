package com.kh.pcar.back.boards.imgBoard.model.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.kh.pcar.back.auth.model.vo.CustomUserDetails;
import com.kh.pcar.back.boards.imgBoard.model.dto.ImgBoardDTO;

public interface ImgBoardService {
	
	void imgSave(ImgBoardDTO imgBoard, MultipartFile file, String username);
	
	List <ImgBoardDTO> imgFindAll(int PageNo);
	
	ImgBoardDTO findByImgBoardNo(Long imgBoard);
	
	ImgBoardDTO imgUpdate(ImgBoardDTO imgBoard, MultipartFile file, Long imgBoardNo, CustomUserDetails userDetails);
	
	void deleteByImgBoardNo(Long imgBoardNo, CustomUserDetails userDetails);
}
