package com.mju.management.domain.post.service;

import static com.mju.management.global.model.Exception.ExceptionList.*;

import java.util.Optional;

import com.mju.management.domain.comment.service.port.CommentRepository;
import com.mju.management.domain.post.controller.response.PostDetailResponse;
import com.mju.management.domain.post.domain.Post;
import com.mju.management.domain.post.infrastructure.PostRepository;
import com.mju.management.domain.post.model.dto.request.CreatePostRequestServiceDto;
import com.mju.management.domain.post.model.dto.request.DeletePostRequestServiceDto;
import com.mju.management.domain.post.model.dto.request.RetrieveDetailPostRequestServiceDto;
import com.mju.management.domain.post.model.dto.request.UpdatePostRequestServiceDto;
import com.mju.management.domain.project.infrastructure.Project;
import com.mju.management.domain.project.infrastructure.ProjectRepository;
import com.mju.management.domain.user.dto.GetUserResponseDto;
import com.mju.management.domain.user.service.UserServiceImpl;
import com.mju.management.global.config.jwtInterceptor.JwtContextHolder;
import com.mju.management.global.model.Exception.ExceptionList;
import com.mju.management.global.model.Exception.UnauthorizedAccessException;
import com.mju.management.global.model.Result.CommonResult;
import com.mju.management.global.service.ResponseService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl {

    private final PostRepository postRepository;
    private final ProjectRepository projectRepository;
    private final UserServiceImpl userService;
    private final ResponseService responseService;
    private final CommentRepository commentRepository;

	public CommonResult createPost(CreatePostRequestServiceDto dto) {
        Optional<Project> optionalProject = projectRepository.findById(dto.projectId());
        if (optionalProject.isEmpty()){
            return responseService.getFailResult(INVALID_PROJECT_ID.getCode(), INVALID_PROJECT_ID.getMessage());
        }
        Project project = optionalProject.get();

        // 요청자가 해당 프로젝트의 팀원인지 확인
        Long userId = JwtContextHolder.getUserId();
        checkMemberAuthorization(project, userId);

        Post post = dto.toEntity(userId);
        project.createPost(post);
        postRepository.save(post);
        return responseService.getSuccessfulResultWithMessage("기획/제작/편집 게시글 작성에 성공하였습니다.");
    }

    @Transactional(readOnly = true)
    public CommonResult retrieveDetailPost(RetrieveDetailPostRequestServiceDto dto) {
        Optional<Project> optionalProject = projectRepository.findById(dto.projectId());
        if (optionalProject.isEmpty()){
            return responseService.getFailResult(INVALID_PROJECT_ID.getCode(), INVALID_PROJECT_ID.getMessage());
        }
        Project project = optionalProject.get();

        // 요청자가 해당 프로젝트의 팀원인지 확인
        checkMemberAuthorization(project, JwtContextHolder.getUserId());

        Optional<Post> optionalPost = postRepository.findById(dto.postId());
        if(optionalPost.isEmpty()){
            return responseService.getFailResult(INVALID_POST_ID.getCode(), INVALID_POST_ID.getMessage());
        }

        Post post = optionalPost.get();
        return responseService.getSingleResult(PostDetailResponse.from(post, userService.getUsername(post.getWriterId())));
    }

    public CommonResult updatePost(UpdatePostRequestServiceDto dto) {
        Optional<Project> optionalProject = projectRepository.findById(dto.projectId());
        if (optionalProject.isEmpty()){
            return responseService.getFailResult(INVALID_PROJECT_ID.getCode(), INVALID_PROJECT_ID.getMessage());
        }
        Project project = optionalProject.get();

        // 요청자가 해당 프로젝트의 팀원인지 확인
        Long userId = JwtContextHolder.getUserId();
        checkMemberAuthorization(project, userId);

        Optional<Post> optionalPost = postRepository.findById(dto.postId());
        if(optionalPost.isEmpty()){
            return responseService.getFailResult(INVALID_POST_ID.getCode(), INVALID_POST_ID.getMessage());
        }

        Post post = optionalPost.get();
        if(post.getWriterId() != userId){
             return responseService.getFailResult(NO_PERMISSION_TO_EDIT_POST.getCode(), NO_PERMISSION_TO_EDIT_POST.getMessage());
        }

        post.update(dto);
        return responseService.getSuccessfulResultWithMessage("기획/제작/편집 게시글 수정에 성공하였습니다.");
    }

    public CommonResult deletePost(DeletePostRequestServiceDto dto) {
        Optional<Project> optionalProject = projectRepository.findById(dto.projectId());
        if (optionalProject.isEmpty()){
            return responseService.getFailResult(INVALID_PROJECT_ID.getCode(), INVALID_PROJECT_ID.getMessage());
        }
        Project project = optionalProject.get();

        // 요청자가 해당 프로젝트의 팀원인지 확인
        Long userId = JwtContextHolder.getUserId();
        checkMemberAuthorization(project, userId);

        Optional<Post> optionalPost = postRepository.findById(dto.postId());
        if(optionalPost.isEmpty()){
            return responseService.getFailResult(INVALID_POST_ID.getCode(), INVALID_POST_ID.getMessage());
        }

        Post post = optionalPost.get();
        if(post.getWriterId() != userId){
            return responseService.getFailResult(NO_PERMISSION_TO_EDIT_POST.getCode(), NO_PERMISSION_TO_EDIT_POST.getMessage());
        }

        // 댓글들 삭제
        commentRepository.deleteAll(post);


        postRepository.delete(post);
        return responseService.getSuccessfulResultWithMessage("기획/제작/편집 게시글 삭제에 성공하였습니다.");
    }


    private void checkMemberAuthorization(Project project, Long userId){
        if(!project.isLeaderOrMember(userId))
            throw new UnauthorizedAccessException(ExceptionList.UNAUTHORIZED_ACCESS);
    }



}
