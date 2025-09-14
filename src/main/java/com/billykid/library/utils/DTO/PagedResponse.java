package com.billykid.library.utils.DTO;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PagedResponse<T> {
    private List<T> content;
    private int page;
    private int sizePerPage;
    private long totalElements;
    private int totalPages;

    public PagedResponse(List<T> content, int page, int sizePerPage, long totalElements, int totalPages) {
        this.content = content;
        this.page = page;
        this.sizePerPage = sizePerPage;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }
}
