package com.kh.pcar.back.boards.comment.model.vo;

import java.sql.Date;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CommentVO {
	private Long commentNo;
	private String commentWriter;
	private String commentContent;
	private Date commentDate;
	private String commentStatus;
	private Long refBno;
}
