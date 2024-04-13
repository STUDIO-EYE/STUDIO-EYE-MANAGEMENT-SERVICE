package com.mju.management.domain.post.model.dto.request;

import com.mju.management.domain.post.infrastructure.Category;

public record UpdatePostRequestServiceDto(
	Long projectId,
	Long postId,
	String title,
	String content,
	Category category

) {
}
