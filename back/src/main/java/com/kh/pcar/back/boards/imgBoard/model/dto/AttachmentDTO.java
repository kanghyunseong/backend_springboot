package com.kh.pcar.back.boards.imgBoard.model.dto;

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
public class AttachmentDTO {
    private Long fileNo;
    private Long refIno;
    private String originName;
    private String changeName;
    private String filePath;
}