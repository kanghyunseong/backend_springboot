package com.kh.pcar.back.admin.boardsDeclaration.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.pcar.back.admin.boardsDeclaration.model.dao.AdminBoardsDeclarationMapper;
import com.kh.pcar.back.admin.boardsDeclaration.model.dto.AdminBoardsDeclarationDTO;
import com.kh.pcar.back.exception.BoardsNotFoundException;
import com.kh.pcar.back.exception.CommentNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminBoardsDeclarationServiceimpl implements AdminBoardsDeclarationService {

	private final AdminBoardsDeclarationMapper adminBoardsDeclarationMapper;

	@Override
	@Transactional(readOnly = true)
	public List<AdminBoardsDeclarationDTO> findAllDeclaration() {
		List<AdminBoardsDeclarationDTO> list = adminBoardsDeclarationMapper.findAllDeclaration();
		if(list.isEmpty()) {
			throw new BoardsNotFoundException("게시글 목록을 찾을 수 없습니다.");
		}
		return list;
	}

	@Override
	@Transactional
	public void deleteDeclaration(Long reportNo) {
		// TODO Auto-generated method stub
		int result = adminBoardsDeclarationMapper.deleteDeclaration(reportNo);
		if (result == 0) {
			throw new BoardsNotFoundException("게시글 번호 " + reportNo + "를 찾을 수 없습니다.");
		}
	}

	@Override
	@Transactional
	public void rejectDeclaration(Long reportNo) {
		int result = adminBoardsDeclarationMapper.rejectDeclaration(reportNo);
		if (result == 0) {
			throw new BoardsNotFoundException("게시글 번호 " + reportNo + "를 찾을 수 없습니다.");
		}
	}

	@Override
	public List<AdminBoardsDeclarationDTO> findAllCommentDeclaration() {
		List<AdminBoardsDeclarationDTO> list = adminBoardsDeclarationMapper.findAllCommentDeclaration();
		return list;
	}

	@Override
	public void deleteCommentDeclaration(Long reportNo) {
		int result = adminBoardsDeclarationMapper.deleteDeclaration(reportNo);
		if (result == 0) {
			throw new BoardsNotFoundException("댓글 번호 " + reportNo + "를 찾을 수 없습니다.");
		}
	}

	@Override
	public void rejectCommentDeclaration(Long reportNo) {
		int result = adminBoardsDeclarationMapper.rejectDeclaration(reportNo);
		if (result == 0) {
			throw new BoardsNotFoundException("댓글 번호 " + reportNo + "를 찾을 수 없습니다.");
		}
	}
}
