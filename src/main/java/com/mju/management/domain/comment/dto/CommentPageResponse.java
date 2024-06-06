package com.mju.management.domain.comment.dto;

import com.mju.management.domain.comment.controller.response.CommentResponse;
import com.mju.management.domain.comment.domain.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
public class CommentPageResponse {

    @Schema(description = "댓글 Id")
    private Long id;

    @Schema(description = "작성 시간 (2023.09.16 11:11)")
    private String createdAt;

    @Schema(description = "댓글")
    private String content;

    @Schema(description = "게시글 Id")
    private Long postId;

    @Schema(description = "수정 시간 (2023.09.16 11:11)")
    private String updatedAt;

    @Schema(description = "댓글 작성자")
    private String userName;

    @Schema(description = "현재 페이지 번호")
    private Integer currentPageNumber;

    @Schema(description = "한 페이지 당 요소 개수")
    private Integer pageSize;

    @Schema(description = "전체 페이지 개수")
    private Integer totalPages;

    @Schema(description = "전체 요소 개수")
    private Integer totalElements;


    public static String changDateFormat(LocalDateTime dateTime){
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm:ss"));
    }

    public static CommentPageResponse from(Comment comment, String username){
        return CommentPageResponse.builder()
                .id(comment.getId())
                .createdAt(changDateFormat(comment.getCreatedAt()))
                .updatedAt(comment.getUpdatedAt() == null ? null : changDateFormat(comment.getUpdatedAt()))
                .content(comment.getContent())
                .userName(username)
                .postId(comment.getPost().getId())
                .build();
    }

}