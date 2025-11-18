package com.kh.pcar.back.boards.imgBoard.model.dto;

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
public class ImgBoardDTO {
   private Long imgBoardNo;
   private String imgBoardTitle;
   private String imgBoardWriter;
   private String imgBoardContent;
   private int imgCount;
   private String fileUrl;
   private String imgBoardStatus;
   private Date imgBoardDate;
}
