package com.kh.pcar.back.boards.imgBoard.model.dto;

import java.sql.Date;
import java.util.List;

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
public class ImgBoardDTO {
   private Long imgBoardNo;
   @NotBlank(message = "제목을 입력해주세요.")
   @Size(max = 100, message = "제목은 100자 이내로 입력해주세요.")
   private String imgBoardTitle;
   private String imgBoardWriter;
   private Long imgWriterNo;
   @NotBlank(message = "내용을 입력해주세요.")
   private String imgBoardContent;
   private int imgCount;
   private String fileUrl;
   private String imgBoardStatus;
   private Date imgBoardDate;
   private List<AttachmentDTO> attachments;
}
