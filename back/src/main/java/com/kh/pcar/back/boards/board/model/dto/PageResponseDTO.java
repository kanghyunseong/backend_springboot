package com.kh.pcar.back.boards.board.model.dto;

import java.util.List;
import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class PageResponseDTO<T> {

    private List<T> content;
    private int totalPages;
    private long totalElements;
    private int page;
    private int size;

}
