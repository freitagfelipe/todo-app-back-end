package com.freitagfelipe.todoapp.domain.authorization.dto;

public record RegisterDTO(
        String username,
        String name,
        String password
) { }
