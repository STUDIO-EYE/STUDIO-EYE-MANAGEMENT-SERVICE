package com.mju.management.domain.comment.service;

import com.mju.management.domain.comment.controller.port.CommentService;
import com.mju.management.domain.comment.domain.Comment;
import com.mju.management.domain.comment.domain.CommentCreate;
import com.mju.management.domain.comment.domain.CommentUpdate;
import com.mju.management.domain.comment.dto.CommentPageRes;
import com.mju.management.domain.comment.service.port.CommentRepository;
import com.mju.management.domain.post.domain.Post;
import com.mju.management.domain.post.infrastructure.PostRepository;
import com.mju.management.domain.project.infrastructure.Project;
import com.mju.management.global.config.jwtinterceptor.JwtContextHolder;
import com.mju.management.global.model.exception.ExceptionList;
import com.mju.management.global.model.exception.NonExistentException;
import com.mju.management.global.model.exception.UnauthorizedAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    private Comment getById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(()
                -> new NonExistentException(ExceptionList.NON_EXISTENT_COMMENT));
    }

    @Override
    public Comment create(Long postId, CommentCreate commentCreate) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 아이디입니다."));

        // 요청자가 해당 프로젝트의 팀원인지 확인
        checkMemberAuthorization(post.getProject(), JwtContextHolder.getUserId());

        Comment comment = Comment.from(post, commentCreate);
        return commentRepository.save(comment);
    }

    @Override
    public CommentPageRes read(Long postId, Integer page, Integer size) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 아이디입니다."));

        // 요청자가 해당 프로젝트의 팀원인지 확인
        checkMemberAuthorization(post.getProject(), JwtContextHolder.getUserId());

        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> commentPage = commentRepository.findCommentsByPost(post, pageable);
        List<Comment> commentList = commentPage.getContent();

        int currentPageNumber = pageable.getPageNumber() + 1;
        int pageSize = pageable.getPageSize();
        int totalPages = commentPage.getTotalPages();
        long totalElements = commentPage.getTotalElements();

        CommentPageRes commentPageRes = CommentPageRes.builder()
                .currentPageNumber(currentPageNumber)
                .pageSize(pageSize)
                .totalPages(totalPages)
                .totalElements((int) totalElements)
                .build();

        post.getCommentList().forEach(commentEntity -> commentPageRes.getCommentList().add(commentEntity.toModel()));

//        List<Comment> comments = new ArrayList<>();
//        post.getCommentList().forEach(commentEntity-> comments.add(commentEntity.toModel()));
//
        return commentPageRes;
    }

    @Override
    public Comment update(Long commentId, CommentUpdate commentUpdate) {
        Comment comment = getById(commentId);
        comment = comment.update(commentUpdate);
        return commentRepository.save(comment);
    }

    @Override
    public void delete(Long commentId) {
        Comment comment = getById(commentId);
        commentRepository.delete(comment);
    }

    private void checkMemberAuthorization(Project project, Long userId){
        if(!project.isLeaderOrMember(userId))
            throw new UnauthorizedAccessException(ExceptionList.UNAUTHORIZED_ACCESS);
    }

}
