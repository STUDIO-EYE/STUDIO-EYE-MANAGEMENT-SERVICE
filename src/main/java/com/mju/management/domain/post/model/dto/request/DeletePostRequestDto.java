package com.mju.management.domain.post.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;

public record DeletePostRequestDto(
	@Positive(message = "projectId 필드의 값은 0 이상이어야 합니다.")
	@Schema(description = "프로젝트를 식별하는 식별자, 0 이하의 값 허용 x")
	Long projectId,

	@Positive(message = "postId 필드의 값은 0 이상이어야 합니다.")
	@Schema(description = "프로젝트를 식별하는 식별자, 0 이하의 값 허용 x")
	Long postId
) {
	public DeletePostRequestServiceDto toServiceRequest() {
		return new DeletePostRequestServiceDto(projectId, postId);
	}
}
