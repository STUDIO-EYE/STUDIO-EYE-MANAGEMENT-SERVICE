package com.mju.management.domain.todo.service;

import com.mju.management.domain.todo.infrastructure.ToDoEntity;
import com.mju.management.domain.todo.dto.ToDoRequestDto;

import java.util.List;

public interface ToDoService {

    public void registerToDo(Long projectId, ToDoRequestDto toDoRequestDto);

    public List<ToDoEntity> getToDo(Long projectId);

    public void deleteToDo(Long todoIndex);

    public ToDoEntity showToDoOne(Long todoIndex);

    public void updateToDo(Long todoIndex, ToDoRequestDto toDoRequestDto);

    public void finishToDo(Long todoIndex);
}
