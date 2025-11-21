package com.kh.pcar.back.boards.imgBoard.model.dto;

import java.util.List;

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
public class ImgPageResponseDTO<T> {

    private List<T> content;
    private int totalPages;
    private long totalElements;
    private int page;
    private int size;

}
