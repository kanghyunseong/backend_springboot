package com.kh.pcar.back.boards.board.model.dto;

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
public class BoardDTO {
	private Long boardNo;
	private String boardTitle;
	private String boardWriter;
	private String boardContent;
	private int count;
	private String boardStatus;
	private Date boardDate;
}
