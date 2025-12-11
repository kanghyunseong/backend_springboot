package com.kh.pcar.back.boards.comment.model.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CommentDTO {
	private Long commentNo;
	private String commentWriter;
	private String commentContent;
	private Date commentDate;
	private String commentStatus;
	private Long refBno;
	
}
