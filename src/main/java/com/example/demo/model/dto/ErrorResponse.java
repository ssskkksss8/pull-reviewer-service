package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ErrorResponse {
    private Error error;

    @Data
    @Builder
    @AllArgsConstructor
    public static class Error {
        private String code;
        private String message;
    }

    public static ErrorResponse of(String code, String message) {
        return ErrorResponse.builder()
                .error(Error.builder().code(code).message(message).build())
                .build();
    }
}
