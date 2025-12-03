package com.kh.pcar.back.admin.notice.model.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AdminNoticeDTO {
	
	private Long noticeNo;
    private String noticeTitle;
    private String noticeWriter;
    private String writerId;
    private String noticeContent;
    private Date noticeDate;
    private int noticeCount;
    private String noticeStatus;

	
}
