package com.mju.management.domain.todo.service;

import com.mju.management.domain.todo.dto.ToDoRequestDto;
import com.mju.management.domain.todo.infrastructure.UserTodo;
import com.mju.management.domain.todo.infrastructure.UserTodoRepository;
import com.mju.management.global.model.Exception.ExceptionList;
import com.mju.management.global.model.Exception.NonExistentException;
import com.mju.management.global.model.Exception.UnauthorizedAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserTodoServiceImpl implements UserTodoService{

    private final UserTodoRepository userTodoRepository;

    // Description : 내 할 일 조회
    @Override
    public List<UserTodo> getMyTodo(Long userId) {
        List<UserTodo> allByUserId = userTodoRepository.findAllByUserId(userId);
        if (!allByUserId.isEmpty())
            return allByUserId;
        else
            throw new NonExistentException(ExceptionList.NON_EXISTENT_CHECKLIST);
    }

    // Description : 내 할 일 하나 조회
    @Override
    public UserTodo showMyToDoOne(Long userTodoId) {

        return userTodoRepository.findById(userTodoId)
                .orElseThrow(() -> new UnauthorizedAccessException(ExceptionList.UNAUTHORIZED_ACCESS));
    }

    // Description : 내 할 일 생성
    @Override
    @Transactional
    public void registerMyToDo(Long userId, ToDoRequestDto toDoRequestDto) {

        UserTodo userTodo = UserTodo.builder()
                .todoContent(toDoRequestDto.getTodoContent())
                .isChecked(false)
                .userId(userId)
                .todoEmergency(toDoRequestDto.isTodoEmergency())
                .build();

        userTodoRepository.save(userTodo);
    }

    // Description : 내 할 일 수정
    @Override
    @Transactional
    public void updateMyToDo(Long userId, Long userTodoId, ToDoRequestDto toDoRequestDto) {

        Optional<UserTodo> findUserTodo = userTodoRepository.findByUserTodoIdAndUserId(userTodoId, userId);
        if (findUserTodo.isPresent()) {
            UserTodo userTodo = findUserTodo.get();
            userTodo.update(toDoRequestDto);

        } else
            throw new NonExistentException(ExceptionList.NON_EXISTENT_PROJECT);

    }

    // Description : 내 할 일 삭제
    @Override
    @Transactional
    public void deleteMyToDo(Long userId, Long userTodoId) {

        Optional<UserTodo> findUserTodo = userTodoRepository.findByUserTodoIdAndUserId(userTodoId, userId);
        if (findUserTodo.isPresent()) {
            UserTodo userTodo = findUserTodo.get();
            userTodoRepository.delete(userTodo);

        } else
            throw new NonExistentException(ExceptionList.NON_EXISTENT_PROJECT);
    }

    // Description : 내 할 일 완료
    @Override
    @Transactional
    public void finishMyToDo(Long userId, Long userTodoId) {

        Optional<UserTodo> findUserTodo = userTodoRepository.findByUserTodoIdAndUserId(userTodoId, userId);
        if (findUserTodo.isPresent()) {
            UserTodo userTodo = findUserTodo.get();
            userTodo.finish(userTodo.isChecked());

        } else
            throw new NonExistentException(ExceptionList.NON_EXISTENT_PROJECT);

    }
    
}
