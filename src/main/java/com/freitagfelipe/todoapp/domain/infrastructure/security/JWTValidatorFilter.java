package com.freitagfelipe.todoapp.domain.infrastructure.security;

import com.freitagfelipe.todoapp.repositories.user.UserRepository;
import com.freitagfelipe.todoapp.services.token.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JWTValidatorFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        Optional<String> token = this.recoverToken(request);

        if (token.isEmpty()) {
            filterChain.doFilter(request, response);

            return;
        }

        Optional<String> result = this.tokenService.validateToken(token.get());

        if (result.isEmpty()) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getOutputStream().print("{ \"message\": \"Invalid token\" }");
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            return;
        }

        String username = result.get();
        UserDetails user = this.userRepository.findByUsername(username);

        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(
                user,
                null,
                user.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(usernamePassword);

        filterChain.doFilter(request, response);
    }

    private Optional<String> recoverToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null) {
            return Optional.empty();
        }

        return Optional.of(
                authHeader.replace("Bearer ", "")
        );
    }

}
