package com.mju.management.domain.comment.dto;

import com.mju.management.domain.comment.domain.Comment;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CommentPageRes {

    private Integer currentPageNumber;

    private Integer pageSize;

    private Integer totalPages;

    private Integer totalElements;

    List<Comment> commentList;

    @Builder
    public CommentPageRes(Integer currentPageNumber, Integer pageSize, Integer totalPages, Integer totalElements) {
        this.currentPageNumber = currentPageNumber;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.commentList = new ArrayList<>();
    }
}
