package com.mju.management.domain.todo.infrastructure;

import com.mju.management.domain.todo.dto.ToDoRequestDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class UserTodo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_todo_id")
    private Long userTodoId;

    @Column(name = "todo_content")
    private String todoContent;

    @Column(name = "is_checked")
    private boolean isChecked;

    @Column(name = "user_id")
    private Long userId;


    @Column(name = "todo_emergency")
    private boolean todoEmergency;


    public void update(ToDoRequestDto toDoRequestDto) {
        this.todoContent = toDoRequestDto.getTodoContent();
        this.isChecked = false;
        this.todoEmergency = toDoRequestDto.isTodoEmergency();
    }

    public void finish(boolean isChecked) {
        if(!isChecked)
            this.isChecked = true;
        else
            this.isChecked = false;
    }

    @Builder
    public UserTodo(Long userTodoId, String todoContent, boolean isChecked, Long userId, boolean todoEmergency) {
        this.userTodoId = userTodoId;
        this.todoContent = todoContent;
        this.isChecked = isChecked;
        this.userId = userId;
        this.todoEmergency = todoEmergency;
    }
}
