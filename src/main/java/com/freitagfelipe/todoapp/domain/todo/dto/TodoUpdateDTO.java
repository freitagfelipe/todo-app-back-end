package com.freitagfelipe.todoapp.domain.todo.dto;

import java.util.Optional;

public record TodoUpdateDTO(
        Long id,
        Optional<String> content,
        Optional<Boolean> state
) { }
