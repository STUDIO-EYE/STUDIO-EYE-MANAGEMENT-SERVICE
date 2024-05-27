package com.mju.management.domain.post.service;

import com.mju.management.domain.comment.service.port.CommentRepository;
import com.mju.management.domain.post.controller.response.PostDetailResponse;
import com.mju.management.domain.post.domain.Post;
import com.mju.management.domain.post.domain.PostFile;
import com.mju.management.domain.post.infrastructure.PostFileRepository;
import com.mju.management.domain.post.infrastructure.PostRepository;
import com.mju.management.domain.post.model.dto.request.CreatePostRequestServiceDto;
import com.mju.management.domain.post.model.dto.request.DeletePostRequestServiceDto;
import com.mju.management.domain.post.model.dto.request.RetrieveDetailPostRequestServiceDto;
import com.mju.management.domain.post.model.dto.request.UpdatePostRequestServiceDto;
import com.mju.management.domain.project.infrastructure.Project;
import com.mju.management.domain.project.infrastructure.ProjectRepository;
import com.mju.management.domain.user.service.UserServiceImpl;
import com.mju.management.global.config.jwtinterceptor.JwtContextHolder;
import com.mju.management.global.model.exception.ExceptionList;
import com.mju.management.global.model.exception.UnauthorizedAccessException;
import com.mju.management.global.model.result.CommonResult;
import com.mju.management.global.service.ResponseService;
import com.mju.management.global.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mju.management.global.model.exception.ExceptionList.*;

@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl {

    private final PostRepository postRepository;
    private final ProjectRepository projectRepository;
    private final UserServiceImpl userService;
    private final ResponseService responseService;
    private final CommentRepository commentRepository;
    private final PostFileRepository postFileRepository;
    private final S3Service s3Service;

	public CommonResult createPost(CreatePostRequestServiceDto dto, List<MultipartFile> files) throws IOException {
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
        // 파일 업로드
        List<PostFile> postFiles = new ArrayList<>();

        if (files != null){
            for (MultipartFile file : files) {
                String s3key = s3Service.uploadFile(file);

                postFiles.add(PostFile.builder()
                        .fileName(file.getOriginalFilename())
                        .filePath(s3Service.getUrl(s3key))
                        .s3key(s3key)
                        .post(post)
                        .project(project)
                        .build());
            }
            postFileRepository.saveAll(postFiles);
        }

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
        return responseService.getSingleResult(PostDetailResponse.from(post,
                post.getWriterId(), userService.getUsername(post.getWriterId())));
    }

    @Transactional(readOnly = true)
    public CommonResult retrieveDetailPostFiles(RetrieveDetailPostRequestServiceDto dto) {
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
        List<PostFile> files = post.getPostFiles();
        return responseService.getListResult(files);
    }
    public CommonResult updatePost(UpdatePostRequestServiceDto dto, List<MultipartFile> newFiles ) throws IOException{
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
        if(!post.getWriterId().equals(userId)){
             return responseService.getFailResult(NO_PERMISSION_TO_EDIT_POST.getCode(), NO_PERMISSION_TO_EDIT_POST.getMessage());
        }

        post.update(dto);
        // 파일 삭제
        List<PostFile> oldFiles = postFileRepository.findByPostId(post.getId());
        for(PostFile file : oldFiles) {
            s3Service.deleteFile(file.getS3key()); //s3 삭제
            postFileRepository.deleteById(file.getId()); //엔티티 삭제
        }
        // 파일 다시 새로 업로드
        if(newFiles!=null){
            List<PostFile> postFiles = new ArrayList<>();
            for (MultipartFile file : newFiles) {
                String s3key = s3Service.uploadFile(file);
                postFiles.add(PostFile.builder()
                        .fileName(file.getOriginalFilename())
                        .filePath(s3Service.getUrl(s3key))
                        .s3key(s3key)
                        .post(post)
                        .project(project)
                        .build());
            }
            postFileRepository.saveAll(postFiles);
        }
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
        if(!post.getWriterId().equals(userId)){
            return responseService.getFailResult(NO_PERMISSION_TO_EDIT_POST.getCode(), NO_PERMISSION_TO_EDIT_POST.getMessage());
        }

        // 댓글들 삭제
        commentRepository.deleteAll(post);

        // 파일 삭제
        List<PostFile> oldFiles = postFileRepository.findByPostId(post.getId());
        for(PostFile file : oldFiles) {
            s3Service.deleteFile(file.getS3key()); //s3 삭제
            postFileRepository.deleteById(file.getId()); //엔티티 삭제
        }
        postRepository.delete(post);
        return responseService.getSuccessfulResultWithMessage("기획/제작/편집 게시글 삭제에 성공하였습니다.");
    }


    private void checkMemberAuthorization(Project project, Long userId){
        if(!project.isLeaderOrMember(userId))
            throw new UnauthorizedAccessException(ExceptionList.UNAUTHORIZED_ACCESS);
    }

}
