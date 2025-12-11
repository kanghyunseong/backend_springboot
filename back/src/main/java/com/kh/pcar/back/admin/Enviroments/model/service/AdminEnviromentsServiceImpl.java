package com.kh.pcar.back.admin.Enviroments.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kh.pcar.back.admin.Enviroments.model.dao.AdminEnviromentsMapper;
import com.kh.pcar.back.admin.Enviroments.model.dto.AdminEnviromentsDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminEnviromentsServiceImpl implements AdminEnviromentsService {

	private final AdminEnviromentsMapper adminEnviromentsMapper;
	@Override
	public List<AdminEnviromentsDTO> findUserRankings() {
		List<AdminEnviromentsDTO> list = adminEnviromentsMapper.findUserRankings();
	
		if(list == null || list.isEmpty()) {
			return List.of();
		}
		return list;
	}

}
