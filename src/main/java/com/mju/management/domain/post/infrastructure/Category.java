package com.mju.management.domain.post.infrastructure;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Category {

	// 기획
	PLANNING("기획"),

	// 제작
	PRODUCTION("제작"),
	
	// 편집
	EDITING("편집");

	private final String description;
}
