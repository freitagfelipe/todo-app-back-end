package com.freitagfelipe.todoapp.controllers.user;

import com.freitagfelipe.todoapp.repositories.user.UserRepository;
import com.freitagfelipe.todoapp.domain.user.dto.UserUpdateDTO;
import com.freitagfelipe.todoapp.domain.user.UserEntity;
import com.freitagfelipe.todoapp.services.token.TokenService;
import com.freitagfelipe.todoapp.utils.ResponseHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Tag(name = "User")
@SecurityRequirement(name = "Bearer")
@RequestMapping("user")
@RestController
public class UserController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository repository;

    @DeleteMapping
    public ResponseEntity<?> delete(
            @Schema(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization
    ) {
        String username = this.tokenService.validateAuthorization(authorization).get();

        if (!this.repository.existsById(username)) {
            return ResponseHandler.generateResponse(
                    Optional.of("This username does not exists"),
                    Optional.empty(),
                    HttpStatus.NOT_FOUND
            );
        }

        this.repository.deleteById(username);

        return ResponseHandler.generateResponse(
                Optional.empty(),
                Optional.empty(),
                HttpStatus.OK
        );
    }

    @PutMapping
    public ResponseEntity<?> update(
            @Schema(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
            @RequestBody UserUpdateDTO user
    ) {
        String username = this.tokenService.validateAuthorization(authorization).get();

        Optional<UserEntity> result = this.repository.findById(username);

        if (result.isEmpty()) {
            return ResponseHandler.generateResponse(
                    Optional.of("This username does not exists"),
                    Optional.empty(),
                    HttpStatus.NOT_FOUND
            );
        }

        UserEntity entity = result.get();

        if (user.name().isPresent()) {
            entity.setName(user.name().get());
        }

        if (user.password().isPresent()) {
            String encryptedPassword = new BCryptPasswordEncoder()
                    .encode(user.password().get());

            entity.setPassword(encryptedPassword);
        }

        this.repository.save(entity);

        return ResponseHandler.generateResponse(
                Optional.empty(),
                Optional.empty(),
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<UserEntity> users = new ArrayList<>();

        this.repository.findAll().forEach(users::add);

        return ResponseHandler.generateResponse(
                Optional.empty(),
                Optional.of(users),
                HttpStatus.OK
        );
    }

}
