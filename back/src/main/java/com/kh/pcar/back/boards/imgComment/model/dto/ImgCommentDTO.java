package com.kh.pcar.back.boards.imgComment.model.dto;

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
public class ImgCommentDTO {
	private Long imgCommentNo;
	private String imgCommentWriter;
	private String imgCommentContent;
	private Date imgCommentDate;
	private String imgCommentStatus;
	private Long refIno;
}
