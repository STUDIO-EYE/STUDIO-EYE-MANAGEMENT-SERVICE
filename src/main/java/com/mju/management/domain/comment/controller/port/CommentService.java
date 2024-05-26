package com.mju.management.domain.comment.controller.port;

import com.mju.management.domain.comment.domain.Comment;
import com.mju.management.domain.comment.domain.CommentCreate;
import com.mju.management.domain.comment.domain.CommentUpdate;

import java.util.List;

public interface CommentService {
    Comment create(Long postId, CommentCreate commentCreate);

    List<Comment> read(Long postId);

    Comment update(Long commentId, CommentUpdate commentUpdate);

    void delete(Long commentId);
}
