package com.freitagfelipe.todoapp.controllers.authorization;

import com.freitagfelipe.todoapp.domain.authorization.dto.LoginDTO;
import com.freitagfelipe.todoapp.domain.authorization.dto.RegisterDTO;
import com.freitagfelipe.todoapp.domain.user.UserEntity;
import com.freitagfelipe.todoapp.domain.user.UserRole;
import com.freitagfelipe.todoapp.repositories.user.UserRepository;
import com.freitagfelipe.todoapp.services.token.TokenService;
import com.freitagfelipe.todoapp.utils.ResponseHandler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Tag(name = "Auth")
@RestController
@RequestMapping("auth")
public class AuthorizationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository repository;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginDTO credentials
    ) {
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(
                credentials.username(), credentials.password()
        );

        try {
            Authentication auth = this.authenticationManager.authenticate(usernamePassword);

            String token = this.tokenService.generateToken((UserEntity) auth.getPrincipal());

            return ResponseHandler.generateResponse(
                    Optional.empty(),
                    Optional.of(token),
                    HttpStatus.OK
            );
        } catch (BadCredentialsException exception) {
            return ResponseHandler.generateResponse(
                    Optional.of("Invalid credentials"),
                    Optional.empty(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterDTO information
    ) {
        if (this.repository.existsById(information.username())) {
            return ResponseHandler.generateResponse(
                    Optional.of("This username already exists"),
                    Optional.empty(),
                    HttpStatus.CONFLICT
            );
        }

        String encryptedPassword = new BCryptPasswordEncoder()
                .encode(information.password());

        UserEntity entity = UserEntity
                .builder()
                .username(information.username())
                .name(information.name())
                .password(encryptedPassword)
                .role(UserRole.USER)
                .build();


        this.repository.save(entity);

        return ResponseHandler.generateResponse(
                Optional.empty(),
                Optional.empty(),
                HttpStatus.OK
        );
    }
}
