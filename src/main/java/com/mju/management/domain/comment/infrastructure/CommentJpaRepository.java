package com.mju.management.domain.comment.infrastructure;

import com.mju.management.domain.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentJpaRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findByPost(Post post);
}
