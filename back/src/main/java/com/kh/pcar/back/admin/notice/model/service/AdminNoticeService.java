package com.kh.pcar.back.admin.notice.model.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.kh.pcar.back.admin.notice.model.dto.AdminNoticeDTO;

public interface AdminNoticeService {
	
	List<AdminNoticeDTO> findAllNotice();
	
	void deleteNotice(Long noticeNo);

    AdminNoticeDTO findNoticeByNo(Long noticeNo);

	void registerNotice(AdminNoticeDTO adminNoticeDTO);
	
	void modifyNotice(AdminNoticeDTO adminNoticeDTO);

}
