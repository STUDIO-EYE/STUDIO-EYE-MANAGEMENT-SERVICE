package com.mju.management.domain.comment.controller;

import com.mju.management.domain.comment.controller.port.CommentService;
import com.mju.management.domain.comment.controller.response.CommentResponse;
import com.mju.management.domain.comment.domain.Comment;
import com.mju.management.domain.comment.domain.CommentCreate;
import com.mju.management.domain.comment.domain.CommentUpdate;
import com.mju.management.domain.comment.dto.CommentPageRes;
import com.mju.management.domain.comment.dto.CommentPageResponse;
import com.mju.management.domain.user.service.UserServiceImpl;
import com.mju.management.global.model.result.CommonResult;
import com.mju.management.global.service.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "[기획 /제작/ 편집] 댓글 작성, 수정, 삭제, 조회 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CommentController {

    private final CommentService commentService;
    private final ResponseService responseService;

    private final UserServiceImpl userService;

    private String getName(long writtenId){
        return userService.getUsername(writtenId);
    }

    @Operation(summary = "댓글 작성")
    @PostMapping("/posts/{postId}/comment")
    public CommonResult create(@PathVariable Long postId,
                               @Valid @RequestBody CommentCreate commentCreate){
        Comment comment = commentService.create(postId, commentCreate);
        return responseService.getSingleResult(CommentResponse.from(comment, getName(comment.getWriteId())));
    }

    @Operation(summary = "게시글 Id에 따른 댓글 읽기")
    @GetMapping("/posts/{postId}/comments")
    public CommonResult read(@PathVariable Long postId, @Positive @RequestParam(value = "page", defaultValue = "1") Integer page, @Positive @RequestParam(value = "pageSize", defaultValue = "5")Integer pageSize){
//        List<Comment> comments = commentService.read(postId, page - 1);
        CommentPageRes comments = commentService.read(postId, page - 1, pageSize);

//        List<CommentResponse> commentResponses = new ArrayList<>();
//        comments.forEach(comment -> commentResponses.add(CommentResponse.from(comment, getName(comment.getWriteId()))));

        List<CommentPageResponse> commentPageResponses = new ArrayList<>();
        comments.getCommentList().forEach(comment -> commentPageResponses.add(CommentPageResponse.from(comment, getName(comment.getWriteId()))));

        return responseService.getSingleResult(commentPageResponses);
    }

    @Operation(summary = "댓글 수정")
    @PutMapping("/comment/{commentId}")
    public CommonResult update(@PathVariable Long commentId,
                               @RequestBody CommentUpdate commentUpdate){
        Comment comment = commentService.update(commentId, commentUpdate);
        return responseService.getSingleResult(CommentResponse.from(comment, getName(comment.getWriteId())));
    }

    @Operation(summary = "댓글 삭제")
    @DeleteMapping("/comment/{commentId}")
    public CommonResult delete(@PathVariable Long commentId){
        commentService.delete(commentId);
        return responseService.getSuccessfulResult();
    }

}
