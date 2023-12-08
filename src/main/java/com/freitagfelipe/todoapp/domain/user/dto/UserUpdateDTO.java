package com.freitagfelipe.todoapp.domain.user.dto;

import java.util.Optional;

public record UserUpdateDTO(
        Optional<String> name,
        Optional<String> password
) { }
