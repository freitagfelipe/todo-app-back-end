package com.freitagfelipe.todoapp.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ResponseHandler {

    public static ResponseEntity<?> generateResponse(
            Optional<String> message,
            Optional<Object> data,
            HttpStatus status
    ) {
        Map<String, Object> map = new HashMap<>();

        map.put("status", status.value());

        message.ifPresent(s -> map.put("message", s));
        data.ifPresent(d -> map.put("data", d));

        return new ResponseEntity<>(map, status);
    }
}
