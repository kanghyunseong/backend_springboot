package com.kh.pcar.back.admin.boardsDeclaration.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.pcar.back.admin.boardsDeclaration.model.dao.AdminBoardsDeclarationMapper;
import com.kh.pcar.back.admin.boardsDeclaration.model.dto.AdminBoardsDeclarationDTO;
import com.kh.pcar.back.exception.BoardsNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminBoardsDeclarationServiceimpl implements AdminBoardsDeclarationService {

	private final AdminBoardsDeclarationMapper adminBoardsDeclarationMapper;

	@Override
	public List<AdminBoardsDeclarationDTO> findAllDeclaration() {
		
		List<AdminBoardsDeclarationDTO> list = adminBoardsDeclarationMapper.findAllDeclaration();
		
		return list;
	}

	@Override
	@Transactional
	public void deleteDelcaration(Long reportNo) {
		// TODO Auto-generated method stub
		int result = adminBoardsDeclarationMapper.deleteDelcaration(reportNo);
		
		if(result == 0) {
			throw new BoardsNotFoundException("게시글 번호 " + reportNo + "를 찾을 수 없습니다.");
		}
	}

	@Override
	@Transactional
	public void rejectDeclaration(Long reportNo) { 
		int result = adminBoardsDeclarationMapper.rejectDeclaration(reportNo);
		
		if(result == 0) {
			throw new BoardsNotFoundException("게시글 번호 " + reportNo + "를 찾을 수 없습니다.");
		}
		
	}
}
