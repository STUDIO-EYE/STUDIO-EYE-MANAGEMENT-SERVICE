package com.mju.management.domain.post.controller.response;

import com.mju.management.domain.post.domain.Post;
import com.mju.management.domain.post.infrastructure.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyPostRes {

    @Schema(description = "게시글 id")
    private Long id;

    @Schema(description = "카테고리")
    private Category category;

    @Schema(description = "게시글 제목")
    private String title;

    @Schema(description = "작성자")
    private String userName;

    @Schema(description = "수정일")
    private LocalDateTime updatedDate;


    public static MyPostRes from(Post post, String userName){

        return MyPostRes.builder()
                .id(post.getId())
                .title(post.getTitle())
                .category(post.getCategory())
                .userName(userName)
                .updatedDate(post.getUpdatedAt())
                .build();
    }
}
