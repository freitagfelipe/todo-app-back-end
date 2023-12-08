package com.freitagfelipe.todoapp.domain.user.dto;

public record UserCreateDTO(
        String name,
        String username,
        String password
) { }
