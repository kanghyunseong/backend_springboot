package com.kh.pcar.back.boards.imgBoard.model.vo;

import java.sql.Date;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ImgBoardVO {
   private Long imgBoardNo;
   private String imgBoardTitle;
   private String imgBoardWriter;
   private Long imgWriterNo;
   private String imgBoardContent;
   private int imgCount;
   private String fileUrl;
   private String imgBoardStatus;
   private Date imgBoardDate;
}
