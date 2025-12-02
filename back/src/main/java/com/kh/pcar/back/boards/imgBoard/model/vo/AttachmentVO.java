package com.kh.pcar.back.boards.imgBoard.model.vo;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AttachmentVO {

    private Long fileNo;
    private Long refIno;
    private String originName;
    private String changeName;
    private String filePath;
    private String status;
}