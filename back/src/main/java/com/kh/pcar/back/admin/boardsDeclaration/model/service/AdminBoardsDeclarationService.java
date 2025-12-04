package com.kh.pcar.back.admin.boardsDeclaration.model.service;

import java.util.List;

import com.kh.pcar.back.admin.boardsDeclaration.model.dto.AdminBoardsDeclarationDTO;

public interface AdminBoardsDeclarationService {
	

	List<AdminBoardsDeclarationDTO> findAllDeclaration();

	void deleteDeclaration(Long reportNo);

	void rejectDeclaration(Long reportNo);

	List<AdminBoardsDeclarationDTO> findAllCommentDeclaration();


	void deleteCommentDeclaration(Long reportNo);

	void rejectCommentDeclaration(Long reportNo);

}
