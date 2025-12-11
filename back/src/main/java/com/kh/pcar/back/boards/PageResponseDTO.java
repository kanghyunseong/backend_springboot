package com.kh.pcar.back.boards;

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
    private int totalPages;
    private long totalElements;
    private int page;
    private int size;
    

	public PageResponseDTO(List<T> content, long totalElements, int page, int size) {
	    this.content = content;
	    this.totalElements = totalElements;
	    this.page = page;
	    this.size = size;
	    this.totalPages = (int) Math.ceil((double) totalElements / size);
	}

}
