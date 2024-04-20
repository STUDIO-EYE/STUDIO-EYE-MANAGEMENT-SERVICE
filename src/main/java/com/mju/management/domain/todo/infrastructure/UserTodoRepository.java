package com.mju.management.domain.todo.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserTodoRepository extends JpaRepository<UserTodo, Long> {

    List<UserTodo> findAllByUserId(Long userId);

    Optional<UserTodo> findByUserTodoIdAndUserId(Long userTodoId, Long userId);
}
