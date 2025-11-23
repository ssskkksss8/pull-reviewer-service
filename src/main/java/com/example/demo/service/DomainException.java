package com.example.demo.service;

import lombok.Getter;

@Getter
public class DomainException extends RuntimeException {
    private final String code;

    public DomainException(String code, String message) {
        super(message);
        this.code = code;
    }

    public static DomainException notFound(String message) {
        return new DomainException("NOT_FOUND", message);
    }
}
