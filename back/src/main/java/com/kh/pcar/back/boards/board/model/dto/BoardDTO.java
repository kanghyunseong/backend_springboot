package com.kh.pcar.back.boards.board.model.dto;

import java.sql.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
	@NotBlank(message = "제목을 입력해주세요.")
    @Size(max = 100, message = "제목은 100자 이내로 입력해주세요.")
	private String boardTitle;
	private String boardWriter;
	private Long writerNo;
	@NotBlank(message = "내용을 입력해주세요.")
	private String boardContent;
	private int count;
	private String boardStatus;
	private Date boardDate;
	
}
