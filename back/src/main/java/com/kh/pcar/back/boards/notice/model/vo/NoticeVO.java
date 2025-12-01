package com.kh.pcar.back.boards.notice.model.vo;

import java.sql.Date;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class NoticeVO {
	private Long noticeNo;
    private String noticeTitle;
    private String noticeWriter;   // USER_ID
    private String noticeContent;
    private Date noticeDate;
    private int noticeCount;
}
