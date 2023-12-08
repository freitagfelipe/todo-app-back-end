package com.freitagfelipe.todoapp.controllers.todo;

import com.freitagfelipe.todoapp.domain.todo.TodoEntity;
import com.freitagfelipe.todoapp.domain.todo.dto.TodoCreateDTO;
import com.freitagfelipe.todoapp.domain.todo.dto.TodoUpdateDTO;
import com.freitagfelipe.todoapp.domain.user.UserEntity;
import com.freitagfelipe.todoapp.repositories.todo.TodoRepository;
import com.freitagfelipe.todoapp.repositories.user.UserRepository;
import com.freitagfelipe.todoapp.services.token.TokenService;
import com.freitagfelipe.todoapp.utils.ResponseHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Tag(name = "Todo")
@SecurityRequirement(name = "Bearer")
@RequestMapping("todo")
@RestController
public class TodoController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> create(
            @Schema(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
            @RequestBody TodoCreateDTO todo
    ) {
        String username = this.tokenService.validateAuthorization(authorization).get();

        System.out.println(username);

        if (!this.userRepository.existsById(username)) {
            return ResponseHandler.generateResponse(
                    Optional.of("This username does not exists"),
                    Optional.empty(),
                    HttpStatus.NOT_FOUND
            );
        }

        UserEntity user = this.userRepository.findById(username).get();

        TodoEntity todoEntity = this.todoRepository.save(
                TodoEntity
                        .builder()
                        .content(todo.content())
                        .done(false)
                        .build()
        );

        user.getTodos().add(todoEntity);

        this.userRepository.save(user);

        return ResponseHandler.generateResponse(
                Optional.empty(),
                Optional.empty(),
                HttpStatus.CREATED
        );
    }

    @PutMapping
    public ResponseEntity<?> update(
            @RequestBody TodoUpdateDTO todo
    ) {
        if (!this.todoRepository.existsById(todo.id())) {
            return ResponseHandler.generateResponse(
                    Optional.of("This todo does not exists"),
                    Optional.empty(),
                    HttpStatus.NOT_FOUND
            );
        }

        TodoEntity todoEntity = this.todoRepository.findById(todo.id()).get();

        if (todo.state().isPresent()) {
            todoEntity.setDone(todo.state().get());
        }

        if (todo.content().isPresent()) {
            todoEntity.setContent(todo.content().get());
        }

        this.todoRepository.save(todoEntity);

        return ResponseHandler.generateResponse(
                Optional.empty(),
                Optional.empty(),
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<?> getTodos(
            @Schema(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization
    ) {
        String username = this.tokenService.validateAuthorization(authorization).get();

        if (!this.userRepository.existsById(username)) {
            return ResponseHandler.generateResponse(
                    Optional.of("This username does not exists"),
                    Optional.empty(),
                    HttpStatus.NOT_FOUND
            );
        }

        UserEntity user = this.userRepository.findById(username).get();

        return ResponseHandler.generateResponse(
                Optional.empty(),
                Optional.of(user.getTodos()),
                HttpStatus.OK
        );
    }

}
