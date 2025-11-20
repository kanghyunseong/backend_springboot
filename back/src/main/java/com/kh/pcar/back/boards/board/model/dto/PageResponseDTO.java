package com.kh.pcar.back.boards.board.model.dto;

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
public class PageResponseDTO<T> {

    private List<T> content;
    private int page;
    private int size;
    private int totalPages;
    private long totalElements;
}
