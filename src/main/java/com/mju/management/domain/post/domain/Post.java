package com.mju.management.domain.post.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.mju.management.domain.comment.infrastructure.CommentEntity;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mju.management.domain.post.infrastructure.Category;
import com.mju.management.domain.post.model.dto.request.UpdatePostRequestServiceDto;
import com.mju.management.domain.project.infrastructure.Project;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	private String title;

	private String content;

	@Enumerated(EnumType.STRING)
	private Category category;

	private int replyCnt = 0;

	private Long writerId;

	@LastModifiedDate
	@Column(nullable = true, updatable = true)
	private LocalDateTime updatedAt;

	@ManyToOne
	@JoinColumn(name = "project_index")
	@JsonIgnore
	private Project project;

	@OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
	private List<CommentEntity> commentList = new ArrayList<>();

//	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	@OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
	private List<PostFile> postFiles;

	@Builder
	public Post(String title, String content, Category category, Long writerId) {
		this.title = title;
		this.content = content;
		this.category = category;
		this.writerId = writerId;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public void update(UpdatePostRequestServiceDto dto) {
		this.title = dto.title();
		this.content = dto.content();
		this.category = dto.category();
	}
}
