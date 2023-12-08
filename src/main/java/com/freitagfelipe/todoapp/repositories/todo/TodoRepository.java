package com.freitagfelipe.todoapp.repositories.todo;

import com.freitagfelipe.todoapp.domain.todo.TodoEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends CrudRepository<TodoEntity, Long> { }
