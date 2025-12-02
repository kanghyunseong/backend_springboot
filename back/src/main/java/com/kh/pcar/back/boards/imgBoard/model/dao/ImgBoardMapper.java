package com.kh.pcar.back.boards.imgBoard.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.kh.pcar.back.boards.imgBoard.model.dto.ImgBoardDTO;
import com.kh.pcar.back.boards.imgBoard.model.vo.ImgBoardVO;

@Mapper
public interface ImgBoardMapper {
	
	void imgSave(ImgBoardVO imgBoard);
	
	List<ImgBoardDTO> imgFindAll(RowBounds rb);
	
	int countImgBoards();
	
	List<ImgBoardDTO> searchImgBoards(Map<String, Object> params);

    int countSearchImgBoards(Map<String, Object> params);
	
	ImgBoardDTO findByImgBoardNo(Long imgBoardNo);
	
	void increaseImgView(Long imgBoardNo);
	
	int imgUpdate(@Param("imgBoard") ImgBoardDTO imgBoard,
	        	  @Param("loginUserNo") Long loginUserNo);
	
	int deleteByImgBoardNo(@Param("imgBoardNo") Long imgBoardNo,
						   @Param("loginUserNo") Long loginUserNo);
	
}
