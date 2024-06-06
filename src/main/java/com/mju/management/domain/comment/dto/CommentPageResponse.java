package com.mju.management.domain.comment.dto;

import com.mju.management.domain.comment.controller.response.CommentResponse;
import com.mju.management.domain.comment.domain.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CommentPageResponse {

    @Schema(description = "현재 페이지 번호")
    private Integer currentPageNumber;

    @Schema(description = "한 페이지 당 요소 개수")
    private Integer pageSize;

    @Schema(description = "전체 페이지 개수")
    private Integer totalPages;

    @Schema(description = "전체 요소 개수")
    private Integer totalElements;

    List<CommentResponse> commentResponses;

    @Builder
    public CommentPageResponse(Integer currentPageNumber, Integer pageSize, Integer totalPages, Integer totalElements) {
        this.commentResponses = new ArrayList<>();
        this.currentPageNumber = currentPageNumber;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }

    public static String changDateFormat(LocalDateTime dateTime){
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm:ss"));
    }

    public static CommentResponse from(Comment comment, String username){
        return CommentResponse.builder()
                .id(comment.getId())
                .createdAt(changDateFormat(comment.getCreatedAt()))
                .updatedAt(comment.getUpdatedAt() == null ? null : changDateFormat(comment.getUpdatedAt()))
                .content(comment.getContent())
                .userName(username)
                .postId(comment.getPost().getId())
                .build();
    }

}