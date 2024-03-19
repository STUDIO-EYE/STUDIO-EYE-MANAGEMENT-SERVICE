package com.mju.management.domain.project.service;

import com.mju.management.domain.project.dto.reqeust.CreateProjectRequestDto;
import com.mju.management.domain.project.dto.response.GetProjectListResponseDto;
import com.mju.management.domain.project.dto.response.GetProjectResponseDto;

import java.util.List;

public interface ProjectService {

    void createProject(CreateProjectRequestDto createProjectRequestDto);
    List<GetProjectListResponseDto> getProjectList();
    List<GetProjectListResponseDto> getMyProjectList();
    GetProjectResponseDto getProject(Long projectIndex);
    void deleteProject(Long projectIndex);
    void updateProject(Long projectIndex, CreateProjectRequestDto createProjectRequestDto);
    void finishProject(Long projectIndex);

}
