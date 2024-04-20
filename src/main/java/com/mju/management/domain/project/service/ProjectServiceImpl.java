package com.mju.management.domain.project.service;

import com.mju.management.domain.post.domain.Post;
import com.mju.management.domain.post.domain.PostFile;
import com.mju.management.domain.post.infrastructure.PostFileRepository;
import com.mju.management.domain.post.infrastructure.PostRepository;
import com.mju.management.domain.project.dto.reqeust.CreateProjectRequestDto;
import com.mju.management.domain.project.dto.response.GetProjectListResponseDto;
import com.mju.management.domain.project.dto.response.GetProjectResponseDto;
import com.mju.management.domain.project.dto.response.GetProjectUserResponseDto;
import com.mju.management.domain.project.infrastructure.*;
import com.mju.management.domain.user.dto.GetUserResponseDto;
import com.mju.management.domain.user.service.UserServiceImpl;
import com.mju.management.global.config.jwtInterceptor.JwtContextHolder;
import com.mju.management.global.model.Exception.ExceptionList;
import com.mju.management.global.model.Exception.NonExistentException;
import com.mju.management.global.model.Exception.StartDateAfterEndDateException;
import com.mju.management.global.model.Exception.UnauthorizedAccessException;
import com.mju.management.global.service.S3Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService{
    private final ProjectRepository projectRepository;
    private final UserServiceImpl userService;
    private final PostRepository postRepository;
    private final PostFileRepository postFileRepository;
    private final S3Service s3Service;

    @Override
    @Transactional
    public void createProject(CreateProjectRequestDto createProjectRequestDto) {
        validateProjectPeriod(createProjectRequestDto);
        Project project = projectRepository.save(createProjectRequestDto.toEntity());

        Set<Long> memberIdList = createProjectRequestDto.getMemberIdList();
        memberIdList.remove(JwtContextHolder.getUserId());

        filterNonExistentMemberId(memberIdList);

        project.getProjectUserList().add(
                ProjectUser.builder()
                        .project(project)
                        .userId(JwtContextHolder.getUserId())
                        .role(Role.LEADER)
                        .build()
        );
        for(Long memberId : memberIdList)
            project.getProjectUserList().add(
                    ProjectUser.builder()
                    .project(project)
                    .userId(memberId)
                    .role(Role.MEMBER)
                    .build()
            );
    }

    @Override
    @Transactional
    public List<GetProjectListResponseDto> getProjectList() {
        List<GetProjectListResponseDto> projectList = projectRepository.findAll()
                .stream().map(GetProjectListResponseDto::from)
                .collect(Collectors.toList());
        if (projectList.isEmpty()) throw new NonExistentException(ExceptionList.NON_EXISTENT_PROJECT);
        return projectList;
    }

    @Override
    public List<GetProjectListResponseDto> getMyProjectList() {
        List<GetProjectListResponseDto> myProjectList = projectRepository.findAllByUserId(JwtContextHolder.getUserId())
                .stream().map(GetProjectListResponseDto::from)
                .collect(Collectors.toList());
        if (myProjectList.isEmpty()) throw new NonExistentException(ExceptionList.NON_EXISTENT_PROJECT);
        return myProjectList;
    }

    @Override
    public GetProjectResponseDto getProject(Long projectId) {
        Project project = projectRepository.findByIdWithProjectUserList(projectId)
                .orElseThrow(()->new NonExistentException(ExceptionList.NON_EXISTENT_PROJECT));
        if(!project.isLeaderOrMember(JwtContextHolder.getUserId()))
            throw new UnauthorizedAccessException(ExceptionList.UNAUTHORIZED_ACCESS);
        List<GetProjectUserResponseDto> getProjectUserResponseDtoList =
                getProjectUserResponseDtoList(project.getProjectUserList());
        return GetProjectResponseDto.from(project, getProjectUserResponseDtoList);
    }

    @Override
    @Transactional
    public void deleteProject(Long projectId) {
        Project project = projectRepository.findByIdWithProjectUserList(projectId)
                .orElseThrow(()->new NonExistentException(ExceptionList.NON_EXISTENT_PROJECT));
        checkLeaderAuthorization(project);
        projectRepository.delete(project);

    }

    @Override
    @Transactional
    public void updateProject(Long projectId, CreateProjectRequestDto projectUpdateRequestDto) {
        Project project = projectRepository.findByIdWithProjectUserList(projectId)
                .orElseThrow(() -> new NonExistentException(ExceptionList.NON_EXISTENT_PROJECT));
        checkLeaderAuthorization(project);
        validateProjectPeriod(projectUpdateRequestDto);
        project.update(projectUpdateRequestDto);

        Set<Long> requestMemberIdList = projectUpdateRequestDto.getMemberIdList();
        requestMemberIdList.remove(JwtContextHolder.getUserId());
        // 요청 dto에 없지만 db에는 있는 팀원을 삭제
        deleteProjectUser(project, requestMemberIdList);

        filterNonExistentMemberId(requestMemberIdList);
        // 요청 dto에 있지만 db에는 없는 팀원을 추가
        addProjectUser(project, requestMemberIdList);

    }

    @Override
    @Transactional
    public void finishProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(()->new NonExistentException(ExceptionList.NON_EXISTENT_PROJECT));
        checkLeaderAuthorization(project);
        project.finish();

    }

    @Override
    public List<PostFile> getProjectFiles(Long projectId) {
        Project project = projectRepository.findByIdWithProjectUserList(projectId)
                .orElseThrow(()->new NonExistentException(ExceptionList.NON_EXISTENT_PROJECT));
        if(!project.isLeaderOrMember(JwtContextHolder.getUserId()))
            throw new UnauthorizedAccessException(ExceptionList.UNAUTHORIZED_ACCESS);
//        List<GetProjectUserResponseDto> getProjectUserResponseDtoList =
//                getProjectUserResponseDtoList(project.getProjectUserList());
        List<PostFile> project_files = postFileRepository.findByProjectId(projectId);
        return project_files;
    }

    @Override
    public void createProjectFiles(Long projectId, List<MultipartFile> files) throws IOException {
        Project project = projectRepository.findByIdWithProjectUserList(projectId)
                .orElseThrow(()->new NonExistentException(ExceptionList.NON_EXISTENT_PROJECT));
        if(!project.isLeaderOrMember(JwtContextHolder.getUserId()))
            throw new UnauthorizedAccessException(ExceptionList.UNAUTHORIZED_ACCESS);
        // 파일 업로드
        List<PostFile> projectFiles = new ArrayList<>();

        if (files != null) {
            for (MultipartFile file : files) {
                String s3key = s3Service.uploadFile(file);

                projectFiles.add(PostFile.builder()
                        .fileName(file.getOriginalFilename())
                        .filePath(s3Service.getUrl(s3key))
                        .s3key(s3key)
                        .project(project)
                        .build());
            }
            postFileRepository.saveAll(projectFiles);
        }
    }

    public void validateProjectPeriod(CreateProjectRequestDto createProjectRequestDto){
        LocalDate startDate = createProjectRequestDto.startDateAsLocalDateType();
        LocalDate endDate = createProjectRequestDto.finishDateAsLocalDateType();
        if(startDate.isAfter(endDate))
            throw new StartDateAfterEndDateException(ExceptionList.START_DATE_AFTER_END_DATE_EXCEPTION);
    }

    // 요청 dto에 있지만 db에는 없는 팀원을 추가
    private void addProjectUser(Project project, Set<Long> requestMemberIdList) {

        Set<Long> existingMemberIdList = project.getMemberIdList();
        for(Long requestMemberId : requestMemberIdList)
            if(!existingMemberIdList.contains(requestMemberId))
                project.getProjectUserList().add(
                        ProjectUser.builder()
                                .project(project)
                                .userId(requestMemberId)
                                .role(Role.MEMBER)
                                .build()
                );

    }

    // 요청 dto에 없지만 db에는 있는 팀원을 삭제
    private void deleteProjectUser(Project project, Set<Long> requestMemberIdList) {
        project.getProjectUserList()
                .removeIf(projectUser -> projectUser.getRole() != Role.LEADER &&
                        !requestMemberIdList.contains(projectUser.getUserId()));
    }

    private void checkLeaderAuthorization(Project project) {
        if(!project.isLeader(JwtContextHolder.getUserId()))
            throw new UnauthorizedAccessException(ExceptionList.UNAUTHORIZED_ACCESS);
    }

    private List<GetProjectUserResponseDto> getProjectUserResponseDtoList(List<ProjectUser> projectUserList) {
        List<GetProjectUserResponseDto> getProjectUserResponseDtoList = new ArrayList<>();
        for(ProjectUser projectUser : projectUserList){
            GetUserResponseDto getUserResponseDto = userService.getUser(projectUser.getUserId());
            if(getUserResponseDto==null) continue;
            GetProjectUserResponseDto getProjectUserResponseDto =
                    GetProjectUserResponseDto.from(getUserResponseDto, projectUser.getRole());
            getProjectUserResponseDtoList.add(getProjectUserResponseDto);
        }
        return getProjectUserResponseDtoList;
    }

    private void filterNonExistentMemberId(Set<Long> memberIdList) {
        memberIdList.removeIf(memberId -> userService.getUser(memberId) == null);
    }
}
