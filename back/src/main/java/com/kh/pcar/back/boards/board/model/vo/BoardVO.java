package com.kh.pcar.back.boards.board.model.vo;

import java.sql.Date;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class BoardVO {
   private Long boardNo;
   private String boardTitle;
   private String boardWriter;
   private String boardContent;
   private int count;
   private String boardStatus;
   private Date boardDate;
}
