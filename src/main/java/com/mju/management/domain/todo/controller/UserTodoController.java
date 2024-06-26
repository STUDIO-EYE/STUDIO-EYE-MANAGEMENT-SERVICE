package com.mju.management.domain.todo.controller;

import com.mju.management.domain.todo.dto.ToDoRequestDto;
import com.mju.management.domain.todo.infrastructure.UserTodo;
import com.mju.management.domain.todo.service.UserTodoService;
import com.mju.management.global.config.jwtinterceptor.JwtContextHolder;
import com.mju.management.global.model.result.CommonResult;
import com.mju.management.global.service.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "유저의 체크리스트 CRUD API", description = "유저의 체크리스트(할일) 관련 API")
@RestController
@RequestMapping("/api/userTodo")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserTodoController {

    private final UserTodoService userTodoService;
    private final ResponseService responseService;

    // 내 할 일 조회
    @GetMapping
    @Operation(summary = "내 할 일 목록 조회", description = "내 할 일 목록 조회 api")
    public CommonResult showMyToDo() {
        List<UserTodo> userTodoList = userTodoService.getMyTodo(JwtContextHolder.getUserId());
        return responseService.getListResult(userTodoList);

    }

    @GetMapping("/{userTodoId}")
    @Operation(summary = "내 할 일 하나 조회", description = "내 할 일 하나 조회 api")
    public CommonResult showMyToDoOne(@PathVariable Long userTodoId) {
        UserTodo userTodo = userTodoService.showMyToDoOne(userTodoId);
        return responseService.getSingleResult(userTodo);
    }

    // 내 할 일 생성
    @PostMapping
    @Operation(summary = "할일 등록", description = "할일 등록 api")
    public CommonResult registerMyToDo(@RequestBody ToDoRequestDto toDoRequestDto){
        userTodoService.registerMyToDo(JwtContextHolder.getUserId(), toDoRequestDto);
        return responseService.getSuccessfulResult();
    }

    // 내 할 일 수정
    @PutMapping("/{userTodoId}")
    @Operation(summary = "내 할 일 수정", description = "내 할 일 수정 api")
    public CommonResult updateMyToDo(
            @PathVariable Long userTodoId,
            @RequestBody ToDoRequestDto toDoRequestDto
    ) {
        userTodoService.updateMyToDo(JwtContextHolder.getUserId(), userTodoId, toDoRequestDto);
        return responseService.getSuccessfulResult();
    }

    // 내 할 일 삭제
    @DeleteMapping("/{userTodoId}")
    @Operation(summary = "내 할 일 삭제", description = "내 할 일 삭제 api")
    public CommonResult deleteMyToDo(@PathVariable Long userTodoId) {
        userTodoService.deleteMyToDo(JwtContextHolder.getUserId(), userTodoId);
        return responseService.getSuccessfulResult();
    }

    // 내 할 일 완료
    @PatchMapping("/checking/{userTodoId}")
    @Operation(summary = "내 할 일 완료 표시", description = "내 할 일 완료 표시 api")
    public CommonResult finishMyToDo(@PathVariable Long userTodoId) {
        userTodoService.finishMyToDo(JwtContextHolder.getUserId(), userTodoId);
        return responseService.getSuccessfulResult();
    }

}
