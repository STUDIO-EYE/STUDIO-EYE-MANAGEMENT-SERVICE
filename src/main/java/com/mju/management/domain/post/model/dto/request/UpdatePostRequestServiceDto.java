package com.mju.management.domain.post.model.dto.request;

import com.mju.management.domain.post.infrastructure.Category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdatePostRequestServiceDto(
	Long projectId,
	Long postId,
	String title,
	String content,
	Category category

) {
}
