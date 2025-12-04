package com.kh.pcar.back.admin.boardsDeclaration.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kh.pcar.back.admin.boardsDeclaration.model.dto.AdminBoardsDeclarationDTO;

@Mapper
public interface AdminBoardsDeclarationMapper {

	List<AdminBoardsDeclarationDTO> findAllDeclaration();

	int deleteDeclaration(Long reportNo);

	int rejectDeclaration(Long reportNo);

	List<AdminBoardsDeclarationDTO> findAllCommentDeclaration();

	int deleteCommentDeclaration(Long reportNo);

	int rejectCommentDeclaration(Long reportNo);

}
