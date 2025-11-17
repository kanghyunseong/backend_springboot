package com.kh.pcar.back.boards.comment.model.vo;

import java.sql.Date;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CommentVO {
	
	private Long commentNo;
	private String commentContent;
	private String commentWriter;
	private Date createDate;
	private Long refBoardNo;
}
