package com.kh.pcar.back.boards.imgBoard.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.kh.pcar.back.boards.imgBoard.model.dto.ImgBoardDTO;
import com.kh.pcar.back.boards.imgBoard.model.vo.ImgBoardVO;

@Mapper
public interface ImgBoardMapper {
	
	void imgSave(ImgBoardVO imgBoard);
	
	List<ImgBoardDTO> imgFindAll(RowBounds rb);
	
	ImgBoardDTO findByImgBoardNo(Long imgBoardNo);
	
	void imgUpdate(ImgBoardDTO imgBoard);
	
	void deleteByImgBoardNo(Long imgBoardNo);
}
