package com.mju.management.domain.comment.infrastructure;

import com.mju.management.domain.comment.domain.Comment;
import com.mju.management.domain.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentJpaRepository extends JpaRepository<CommentEntity, Long> {
    List<Comment> findByPost(Post post);

    Page<Comment> findCommentsByPost(Post post, Pageable pageable);
}
