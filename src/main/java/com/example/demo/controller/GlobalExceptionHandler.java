package com.example.demo.controller;

import com.example.demo.model.dto.ErrorResponse;
import com.example.demo.service.DomainException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomain(DomainException ex) {
        HttpStatus status = switch (ex.getCode()) {
            case "TEAM_EXISTS" -> HttpStatus.BAD_REQUEST;
            case "PR_EXISTS" -> HttpStatus.CONFLICT;
            case "PR_MERGED", "NOT_ASSIGNED", "NO_CANDIDATE" -> HttpStatus.CONFLICT;
            case "NOT_FOUND" -> HttpStatus.NOT_FOUND;
            default -> HttpStatus.BAD_REQUEST;
        };
        return ResponseEntity.status(status).body(ErrorResponse.of(ex.getCode(), ex.getMessage()));
    }
}
