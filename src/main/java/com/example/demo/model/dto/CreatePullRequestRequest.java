package com.example.demo.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreatePullRequestRequest {

    @NotBlank
    private String title;

    @NotNull
    private UUID authorId;

}
