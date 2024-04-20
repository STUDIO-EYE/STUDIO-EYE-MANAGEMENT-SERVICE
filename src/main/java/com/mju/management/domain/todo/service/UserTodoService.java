package com.mju.management.domain.todo.service;

import com.mju.management.domain.todo.dto.ToDoRequestDto;
import com.mju.management.domain.todo.infrastructure.UserTodo;

import java.util.List;

public interface UserTodoService {

    List<UserTodo> getMyTodo(Long userId);

    void registerMyToDo(Long userId, ToDoRequestDto toDoRequestDto);

    void updateMyToDo(Long userId, Long userTodoId, ToDoRequestDto toDoRequestDto);

    void deleteMyToDo(Long userId, Long userTodoId);

}
