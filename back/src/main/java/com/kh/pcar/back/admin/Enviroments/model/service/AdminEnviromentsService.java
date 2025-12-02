package com.kh.pcar.back.admin.Enviroments.model.service;

import java.util.List;

import com.kh.pcar.back.admin.Enviroments.model.dto.AdminEnviromentsDTO;

public interface AdminEnviromentsService {

	List<AdminEnviromentsDTO> findUserRankings();
}
