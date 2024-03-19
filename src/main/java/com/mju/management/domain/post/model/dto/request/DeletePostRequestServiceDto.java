package com.mju.management.domain.post.model.dto.request;

public record DeletePostRequestServiceDto(
	Long projectId,
	Long postId
) {
}
