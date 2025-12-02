package com.kh.pcar.back.admin.notice.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kh.pcar.back.admin.notice.model.dto.AdminNoticeDTO;

@Mapper
public interface AdminNoticeMapper {
	
	List<AdminNoticeDTO> findAllNotice();
	
	int deleteNotice(Long noticeNo);

	AdminNoticeDTO findNoticeByNo(Long noticeNo);

	void insertNotice(AdminNoticeDTO adminNoticeDTO);
	
	int updateNotice(AdminNoticeDTO adminNoticeDTO);

}
