package com.kh.pcar.back.boards.imgComment.model.vo;

import java.sql.Date;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ImgCommentVO {
	private Long imgCommentNo;
	private String imgCommentWriter;
	private String imgCommentContent;
	private Date imgCommentDate;
	private String imgCommentStatus;
	private Long refIno;
}
