package com.mju.management.domain.comment.service.port;

import com.mju.management.domain.comment.domain.Comment;
import com.mju.management.domain.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CommentRepository {

    Optional<Comment> findById(Long commentId);

    Comment save(Comment comment);

    void delete(Comment comment);

    void deleteAll(Post post);

    Page<Comment> findCommentsByPost(Post post, Pageable pageable);
}
