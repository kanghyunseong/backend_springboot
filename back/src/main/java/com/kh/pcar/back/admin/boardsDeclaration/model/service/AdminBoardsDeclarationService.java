package com.kh.pcar.back.admin.boardsDeclaration.model.service;

import java.util.List;

import com.kh.pcar.back.admin.boardsDeclaration.model.dto.AdminBoardsDeclarationDTO;

public interface AdminBoardsDeclarationService {
	

	List<AdminBoardsDeclarationDTO> findAllDeclaration();

	void deleteDelcaration(Long reportNo);

}
