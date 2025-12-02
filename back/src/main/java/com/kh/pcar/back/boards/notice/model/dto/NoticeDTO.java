package com.kh.pcar.back.boards.notice.model.dto;

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
public class NoticeDTO {
	
	private Long noticeNo;
    private String noticeTitle;
    private String noticeWriter;   // USER_ID
    private String noticeContent;
    private Date noticeDate;
    private int noticeCount;
    
}
